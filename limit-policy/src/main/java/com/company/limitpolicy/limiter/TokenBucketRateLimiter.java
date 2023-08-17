package com.company.limitpolicy.limiter;

/**
 * @author wei.song
 * @since 2023/8/17 0:21
 */
public class TokenBucketRateLimiter implements RateLimiter {

    /**
     * 当前令牌数
     */
    private int tokens;
    /**
     * 令牌桶容量
     */
    private final int capacity;
    /**
     * 令牌生成速率，单位：令牌/秒
     */
    private final int rate;
    /**
     * 上次令牌生成时间戳
     */
    private long lastRefillTimestamp;

    public TokenBucketRateLimiter(int capacity, int rate) {
        this.capacity = capacity;
        this.rate = rate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean acquire(int count) {
        // 每次进入同统计一下这段时间内生成的令牌数量
        long now = System.currentTimeMillis();
        if (now > lastRefillTimestamp) {
            int generatedTokens = (int) ((now - lastRefillTimestamp) / 1000 * rate);
            tokens = Math.min(tokens + generatedTokens, capacity);
            lastRefillTimestamp = now;
        }

        // 判断令牌数是否满足要求
        if (tokens - count >= 0) {
            tokens -= count;
            return true;
        }
        return false;
    }
}
