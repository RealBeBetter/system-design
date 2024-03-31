package com.company.limitpolicy.annotation.aspect;

import com.company.limitpolicy.annotation.Limiter;
import com.company.limitpolicy.annotation.RateLimiterType;
import com.company.limitpolicy.limiter.RateLimiter;
import com.company.limitpolicy.service.RateLimiterFactory;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Real
 * @since 2024/3/30 18:14
 */
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

    @Pointcut("@annotation(com.company.limitpolicy.annotation.Limiter)")
    public void pointcut() {

    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature sign = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = sign.getMethod();
        Limiter limiter = method.getAnnotation(Limiter.class);

        RateLimiterType type = limiter.type();
        int acceptCnt = limiter.acceptCnt();

        RateLimiter rateLimiter = RateLimiterFactory.createRateLimiter(type, acceptCnt);
        if (!rateLimiter.acquire()) {
            log.warn("请求被限流，方法：{}", method.getName());
        }

        return proceedingJoinPoint.proceed();
    }

}

