package com.company.limitpolicy.limiter;

/**
 * @author wei.song
 * @since 2023/8/16 22:57
 */
public interface RateLimiter {

    /**
     * 实现一个限流器，每秒只能通过 max 个请求；
     * 如果超过，那么acquire返回false；需要考虑并发性能问题
     *
     * @return boolean
     */

    default boolean acquire() {
        return acquire(1);
    }

    /**
     * 申请资源
     *
     * @param count 计数
     * @return boolean 是否通过
     */

    boolean acquire(int count);

}
