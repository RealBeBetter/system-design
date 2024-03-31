package com.company.limitpolicy.annotation.spring;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Real
 * @since 2024/3/31 17:53
 */
@Configuration
public class RateLimiterConfig {
    @Bean
    public DefaultPointcutAdvisor defaultPointcutAdvisor() {
        RateLimiterInterceptor interceptor = new RateLimiterInterceptor();

        // AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(Limiter.class, true);
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        pointcut.setPatterns("com.company.*.annotation.Limiter");

        // 配置增强类advisor
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        advisor.setPointcut(pointcut);
        advisor.setAdvice(interceptor);
        return advisor;
    }
}
