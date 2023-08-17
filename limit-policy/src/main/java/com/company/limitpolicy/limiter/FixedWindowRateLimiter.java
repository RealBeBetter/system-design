package com.company.limitpolicy.limiter;

/**
 * @author wei.song
 * @since 2023/8/16 23:56
 */
public class FixedWindowRateLimiter implements RateLimiter {

    public static int counter = 0;
    public static long lastAcquireTime = 0L;

    /**
     * 窗口时间长度，单位 ms
     */
    private final long windowUnit;
    /**
     * 可以接受的请求次数
     */
    private final long canAcceptRequestTimes;

    public FixedWindowRateLimiter(long windowUnit, long canAcceptRequestTimes) {
        this.windowUnit = windowUnit;
        this.canAcceptRequestTimes = canAcceptRequestTimes;
    }

    @Override
    public boolean acquire(int count) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastAcquireTime > windowUnit) {
            counter = 0;
            lastAcquireTime = currentTimeMillis;
        }

        if (counter < canAcceptRequestTimes) {
            counter++;
            return true;
        }

        return false;
    }
}
