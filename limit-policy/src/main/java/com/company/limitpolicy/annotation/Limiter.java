package com.company.limitpolicy.annotation;

import java.lang.annotation.*;

/**
 * @author Real
 * @since 2024/3/30 18:13
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limiter {

    /**
     * 表示使用什么类型的限流方法
     *
     * @return {@link RateLimiterType}
     */
    RateLimiterType type() default RateLimiterType.counter;

    /**
     * 每秒可以接受的请求数
     *
     * @return acceptCnt
     */
    int acceptCnt() default 1000;

}
