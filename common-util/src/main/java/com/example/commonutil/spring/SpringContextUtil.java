package com.example.commonutil.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author wei.song
 * @since 2023/6/26 19:16
 */
@Slf4j
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    /**
     * 传入线程中
     *
     * @param beanName bean名字
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) context.getBean(beanName);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return context.getBean(beanName, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    /**
     * 国际化使用
     *
     * @param key Key键
     * @return {@link String}
     */
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }


    /**
     * 获取当前环境
     *
     * @return {@link String}
     */
    public static String getActiveProfile() {
        for (String active : context.getEnvironment().getActiveProfiles()) {
            return active;
        }

        return null;
    }
}
