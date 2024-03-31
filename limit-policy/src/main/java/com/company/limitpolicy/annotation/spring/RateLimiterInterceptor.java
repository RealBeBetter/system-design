package com.company.limitpolicy.annotation.spring;

import com.company.limitpolicy.annotation.Limiter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Real
 * @since 2024/3/31 17:46
 */
@Slf4j
@Component
public class RateLimiterInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Limiter annotation = getAnnotation(method);
        if (annotation == null) {
            return invocation.proceed();
        }

        log.info("method [{}] is called on {} with args {}", method.getName(), invocation.getThis(), invocation.getArguments());
        Object proceed = invocation.proceed();
        log.info("method [{}] returns {}", method.getName(), proceed);

        return proceed;
    }

    private Limiter getAnnotation(Method method) {
        // 这里考虑目标方法上有多个注解的情况
        if (method.isAnnotationPresent(Limiter.class)) {
            return method.getAnnotation(Limiter.class);
        }

        return null;
    }

}
