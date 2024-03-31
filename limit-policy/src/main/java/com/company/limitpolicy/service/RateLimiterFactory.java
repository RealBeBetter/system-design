package com.company.limitpolicy.service;

import com.company.limitpolicy.annotation.RateLimiterType;
import com.company.limitpolicy.limiter.*;

/**
 * @author wei.song
 * @since 2023/8/17 10:50
 */
public class RateLimiterFactory {

    public static RateLimiter createRateLimiter(RateLimiterType rateLimiterType, int rate) {
        switch (rateLimiterType) {
            case counter:
                return new CountRateRateLimiter(rate);
            case fixed_window:
                return new FixedWindowRateLimiter(1000L, rate);
            case sliding_window:
                return new SlidingWindowRateLimiter(10, rate);
            case leak_bucket:
                return new LeakBucketRateLimiter(10, rate);
            case token_bucket:
                return new TokenBucketRateLimiter(100, rate);
            default:
                throw new IllegalArgumentException("Unknown rate limiter type");
        }
    }

}
