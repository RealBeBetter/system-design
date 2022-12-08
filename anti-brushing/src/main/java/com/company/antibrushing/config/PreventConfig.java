package com.company.antibrushing.config;

import com.alibaba.fastjson.JSON;
import com.company.antibrushing.annotation.Prevent;
import com.company.antibrushing.common.BusinessException;
import com.company.antibrushing.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Real
 * @since 2022/12/9 0:44
 */
@Aspect
@Component
public class PreventConfig {

    private static final Logger log = LoggerFactory.getLogger(PreventConfig.class);

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 切入点
     */
    @Pointcut("@annotation(com.company.antibrushing.annotation.Prevent)")
    public void pointcut() {
    }


    /**
     * 处理前
     */
    @Before("pointcut()")
    public void joinPoint(JoinPoint joinPoint) throws Exception {
        // 获取请求参数
        String requestStr = JSON.toJSONString(joinPoint.getArgs()[0]);
        // 校验请求参数是否为空，如果为空则抛出业务异常
        if (StringUtils.isEmpty(requestStr) || requestStr.equalsIgnoreCase("{}")) {
            throw new BusinessException("[防刷]入参不允许为空");
        }
        // 获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取方法
        Method method = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(),
                methodSignature.getParameterTypes());
        // 获取方法上的@Prevent注解
        Prevent preventAnnotation = method.getAnnotation(Prevent.class);
        // 获取方法的完整名称
        String methodFullName = method.getDeclaringClass().getName() + method.getName();
        // 进入入口处理方法
        entrance(preventAnnotation, requestStr, methodFullName);
    }


    /**
     * 入口
     *
     * @param prevent    注解
     * @param requestStr 请求参数
     */
    private void entrance(Prevent prevent, String requestStr, String methodFullName) throws Exception {
        PreventStrategy strategy = prevent.strategy();
        switch (strategy) {
            case DEFAULT:
                defaultHandle(requestStr, prevent, methodFullName);
                break;
            default:
                throw new BusinessException("无效的策略");
        }
    }


    /**
     * 默认处理方式
     *
     * @param prevent    注解
     * @param requestStr 请求参数
     */
    private void defaultHandle(String requestStr, Prevent prevent, String methodFullName) throws Exception {
        String base64Str = toBase64String(requestStr);
        long expire = Long.parseLong(prevent.value());

        String resp = (String) redisUtil.get(methodFullName + base64Str);
        if (StringUtils.isEmpty(resp)) {
            redisUtil.set(methodFullName + base64Str, requestStr, expire);
        } else {
            String message = !StringUtils.isEmpty(prevent.message()) ? prevent.message() :
                    expire + "秒内不允许重复请求";
            throw new BusinessException(message);
        }
    }


    /**
     * 对象转换为base64字符串
     *
     * @param obj 对象值
     * @return base64字符串
     */
    private String toBase64String(String obj) {
        if (StringUtils.isEmpty(obj)) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = obj.getBytes(StandardCharsets.UTF_8);
        return encoder.encodeToString(bytes);
    }
}
