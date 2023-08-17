package com.company.limitpolicy.limiter;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author wei.song
 * @since 2023/8/16 22:54
 */
public class CountRateRateLimiter implements RateLimiter {

    /**
     * 一秒可以接受多少个请求
     */
    private final int numAcceptablePerSecond;

    /***
     * 版本号对应秒数
     * 里面的 AtomicInteger 记录这一秒范围内的请求数量
     */
    private final AtomicStampedReference<AtomicInteger> helper;

    public CountRateRateLimiter(int numAcceptablePerSecond) {
        this.numAcceptablePerSecond = numAcceptablePerSecond;
        this.helper = new AtomicStampedReference<>(new AtomicInteger(numAcceptablePerSecond), -1);
    }


    @Override
    public boolean acquire(int n) {
        if (n > numAcceptablePerSecond) {
            return false;
        }

        // 上一次请求是多少秒的请求
        int oldSeconds = helper.getStamp();
        // 当前多少秒
        int currentSeconds = currentSeconds();

        // 不是同一秒的请求
        // 如果和当前不是一个版本（意味着不是同一秒） 那么cas 修改版本并重置许可数量
        if (oldSeconds != currentSeconds) {
            // 原剩余的许可数量
            AtomicInteger oldPermits = helper.getReference();
            // cas 修改 同时修改版本，并且扣减数量；新许可的数量为 numAcceptablePerSecond - n，避免为负数
            if (helper.compareAndSet(oldPermits, new AtomicInteger(numAcceptablePerSecond - n), oldSeconds, currentSeconds)) {
                // cas 成功 那么说明成功 拿到令牌
                return true;
            }
        }

        // 到这里说明 是同一秒（oldSeconds == currentSeconds）
        // 或者上面的 if 存在多线程竞争当前线程竞争失败 其他线程重置了计数器 ==> 那么 cas 减少许可数量

        // 这里判断了一下 当前秒还是相等的，避免由于 gc 在第一个if中停留太久
        // 比如第一秒线程A和B进入到第一个if，线程B成功了，但是线程A失败了，并且暂停了2s，出来的时候时间已经是3s了，我们不能让1s的请求占用3s时候的令牌数
        return currentSeconds() == currentSeconds
                // 最后这里存在问题 如果在0.99999s的请求来到这里，但是时间来到1s，这个cas才成功，那么0.99999s的请求将打过来。导致1s的qps大于max
                && helper.getReference().addAndGet(-n) >= 0;
    }

    private static int currentSeconds() {
        return (int) ((System.currentTimeMillis() / 1000) % Integer.MAX_VALUE);
    }

}

