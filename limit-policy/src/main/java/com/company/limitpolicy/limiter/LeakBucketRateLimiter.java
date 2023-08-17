package com.company.limitpolicy.limiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 漏漏桶速率限制器
 *
 * @author wei.song
 * @since 2023/08/17 00:05
 */
public class LeakBucketRateLimiter implements RateLimiter {

    // 桶的大小
    private final long CAPACITY;
    // 流出速率，每秒两个
    private final long RATE;
    //开始时间
    private static long startTime = System.currentTimeMillis();
    //桶中剩余的水
    private static final AtomicLong WATER = new AtomicLong();

    public LeakBucketRateLimiter(long capacity, long rate) {
        CAPACITY = capacity;
        RATE = rate;
    }

    @Override
    public synchronized boolean acquire(int count) {
        // 如果桶里是空的，表示第一次使用，判断是否能直接通过
        if (WATER.get() == 0 && count <= CAPACITY) {
            startTime = System.currentTimeMillis();
            WATER.set(count);
            return true;
        }

        long currentTimeMillis = System.currentTimeMillis();
        // 判断过往时间内流出的水量，得出现在桶内的剩余量
        long currentBucketSize = WATER.get() - (currentTimeMillis - startTime) / 1000 * RATE;
        // 防止出现 <0 的情况
        WATER.set(Math.max(0, currentBucketSize));

        // 设置新的开始时间
        startTime += (currentTimeMillis - startTime) / 1000 * 1000;

        // 如果申请的数量 + 现有的数量 < 容量，直接放行
        if (WATER.get() + count <= CAPACITY) {
            WATER.getAndAdd(count);
            return true;
        }

        return false;
    }
}
