package com.company.limitpolicy.limiter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 滑动窗口限流器
 * <p>
 * 假设指定窗口总时长 为 1s 可以接受 10个请求，窗口分为5格
 * 说明单格时间长度为200毫秒
 * |_____|_____|_____|_____|_____|
 * 0    200   400  600    800   1000
 * <p>
 * 当前时间为 500毫秒 那么落在 （500/200）%5 也就是第二格
 * 那么500 毫秒是否可以接受请求 需要统计所有格子中的数量
 * <p>
 * 当时间来到 1500 毫秒，落在 （1500/200）%5 也是第二格
 * |_____|_____|_____|_____|_____|_____|_____|_____|
 * 0    200   400  600    800  1000   1200  1400  1600
 * 从500到1500才是我们需要记录的，窗口数组大小是不变的
 * <p>
 * 500的窗口版本是 500/1000 = 0
 * 1500的窗口版本是 1500/1000 = 1
 * <p>
 * 根据窗口版本来统计 哪些格子我们是要统计的，如果旧窗口版本小于当前窗口版本 不要计数
 * （这里的版本 可以理解为没过 1000秒 版本加1，版本不同意味着是一秒前的数据）
 *
 * @author wei.song
 * @date 2023/08/16 23:25
 */
public class SlidingWindowRateLimiter implements RateLimiter {

    /**
     * 滑动窗口元素
     *
     * @author wei.song
     * @date 2023/08/16 23:26
     */
    private static class WindowElement {
        /***
         * 版本
         */
        private volatile long version;
        /**
         * 计数
         */
        private final AtomicInteger counter;

        private WindowElement(long version, AtomicInteger counter) {
            this.version = version;
            this.counter = counter;
        }

        private void changeVersion(long newVersion) {
            this.version = newVersion;
        }

        private void reset(int n) {
            counter.set(n);
        }

        void add(int n) {
            counter.addAndGet(n);
        }
    }

    /**
     * 整个窗口的大小，比如一秒 只能接受100个请求 那么此值设置为1000（毫秒）
     */
    private final long windowTimeMillions = 1000;
    /***
     * 窗口的长度，窗口的长度，窗口越长，越能防止临界问题
     */
    private final int windowLength;
    /***
     * 窗口数组
     */
    private final AtomicReferenceArray<WindowElement> slidWindow;
    /***
     * 一秒接受 100个请求 那么此值设置为 100
     */
    private final int canAcceptRequestTimes;
    /**
     * 记录 窗口每一个元素  对应的时间跨度
     * 1秒接受100个请求 那么此处为 1000（毫秒）/100 = 10毫秒
     */
    private final int millionsEachOne;

    /**
     * @param windowLength          指定窗口数量
     * @param canAcceptRequestTimes 在 1s 内可以接受多少个请求
     */
    public SlidingWindowRateLimiter(int windowLength,
                                    int canAcceptRequestTimes) {
        this.windowLength = windowLength;
        this.canAcceptRequestTimes = canAcceptRequestTimes;
        slidWindow = new AtomicReferenceArray<>(new WindowElement[windowLength]);
        millionsEachOne = (int) (windowTimeMillions / windowLength);
    }

    @Override
    public boolean acquire(int n) {
        // 1s分为5格 那么 一格200ms
        // 当前时间为 500毫秒 那么落在 （500/200）%5 也就是第二格
        long currentTimeMillis = System.currentTimeMillis();
        // 这次请求 落在 哪个桶
        int index = (int) ((currentTimeMillis / millionsEachOne) % windowLength);
        // 当前这次请求的 version 即当前是多少秒
        long version = currentTimeMillis - currentTimeMillis % windowTimeMillions;
        // 1. 拿到当前当前的计数
        // 1.1 如果计数为空 说明从来没有其他请求设置元素，这时，我们需要cas初始化结束计数
        // 1.2 如果计数不为空
        // 1.2.1 是相同的版本 那么自增计数
        // 1.2.3 如果不是相同的版本（之前版本小于当前版本），那么更新版本
        // 1.2.4 如果不是相同的版本（之前版本大于当前版本），基本上不可能，因为时间是不会倒流的

        //操作这次请求落下的桶
        WindowElement currentIndex = slidWindow.accumulateAndGet(index,
                new WindowElement(version, new AtomicInteger(n)), (old, now) -> {
                    // 计数为空 说明从来没有其他请求设置元素，这时，我们需要cas初始化结束计数
                    if (old == null) {
                        return now;
                    }

                    // 当前请求的次数
                    int currentRequest = now.counter.get();

                    // 是同一秒 那么自增
                    if (old.version == now.version) {
                        old.add(currentRequest);
                    } else {
                        // 如果不是相同的版本（之前版本小于当前版本），那么更新版本 更新计数
                        old.reset(currentRequest);
                        old.changeVersion(now.version);
                    }
                    return old;
                });

        // 大于最大数量返回false 这一瞬间对应的元素 就已经超出了我们的预期 那么返回 false
        if (currentIndex.counter.get() > canAcceptRequestTimes) {
            return false;
        }

        // 统计窗口内所有请求数
        long sum = 0;
        // 下面这一段 不具备瞬时一致性；需要考虑计数耗费的时间，理想情况下需要达到 O(1)
        for (int i = 0; i < windowLength; i++) {
            WindowElement e = slidWindow.get(i);
            // 要注意版本的一致性，版本不一致时，表示不是在一个时间周期内，为历史数据
            if (e != null && e.version == version) {
                sum += e.counter.get();
                if (sum > canAcceptRequestTimes) {
                    return false;
                }
            }
        }
        // 小于等于才通过
        return sum <= canAcceptRequestTimes;
    }

}
