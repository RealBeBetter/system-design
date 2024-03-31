package com.company.limitpolicy.annotation;

/**
 * @author Real
 * @since 2024/3/30 18:30
 */
public enum RateLimiterType {

    counter,

    fixed_window,

    sliding_window,

    leak_bucket,

    token_bucket
}
