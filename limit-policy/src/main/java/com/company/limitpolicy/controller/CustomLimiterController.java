package com.company.limitpolicy.controller;

import com.company.limitpolicy.entity.Result;
import com.company.limitpolicy.limiter.RateLimiter;
import com.company.limitpolicy.service.RateLimiterFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wei.song
 * @since 2023/8/17 10:55
 */
@RestController
@RequestMapping("/custom")
public class CustomLimiterController {

    public static final RateLimiter COUNTER_LIMITER = RateLimiterFactory.createRateLimiter(RateLimiterFactory.RateLimiterType.counter, 2);
    public static final RateLimiter FIXED_WINDOW_LIMITER = RateLimiterFactory.createRateLimiter(RateLimiterFactory.RateLimiterType.fixed_window, 2);
    public static final RateLimiter SLIDING_WINDOW_LIMITER = RateLimiterFactory.createRateLimiter(RateLimiterFactory.RateLimiterType.sliding_window, 2);
    public static final RateLimiter LEAK_BUCKET_LIMITER = RateLimiterFactory.createRateLimiter(RateLimiterFactory.RateLimiterType.leak_bucket, 2);
    public static final RateLimiter TOKEN_BUCKET_LIMITER = RateLimiterFactory.createRateLimiter(RateLimiterFactory.RateLimiterType.token_bucket, 2);

    @GetMapping("/counter")
    public Result<String> testCounter() {
        boolean acquire = COUNTER_LIMITER.acquire();
        if (acquire) {
            return Result.success();
        }

        throw new RuntimeException("rate limited... please wait...");
    }


    @GetMapping("/fixedWindow")
    public Result<String> testFixedWindow() {
        boolean acquire = FIXED_WINDOW_LIMITER.acquire();
        if (acquire) {
            return Result.success();
        }

        throw new RuntimeException("rate limited... please wait...");
    }

    @GetMapping("/slidingWindow")
    public Result<String> testSlidingWindow() {
        boolean acquire = SLIDING_WINDOW_LIMITER.acquire();
        if (acquire) {
            return Result.success();
        }

        throw new RuntimeException("rate limited... please wait...");
    }

    @GetMapping("/leakBucket")
    public Result<String> testLeakBucket() {
        boolean acquire = LEAK_BUCKET_LIMITER.acquire();
        if (acquire) {
            return Result.success();
        }

        throw new RuntimeException("rate limited... please wait...");
    }

    @GetMapping("/tokenBucket")
    public Result<String> testTokenBucket() {
        boolean acquire = TOKEN_BUCKET_LIMITER.acquire();
        if (acquire) {
            return Result.success();
        }

        throw new RuntimeException("rate limited... please wait...");
    }

}
