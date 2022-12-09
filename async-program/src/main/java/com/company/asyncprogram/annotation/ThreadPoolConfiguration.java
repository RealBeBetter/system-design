package com.company.asyncprogram.annotation;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Real
 * @since 2022/12/9 1:45
 */
@Configuration
public class ThreadPoolConfiguration {

    public static final Logger log = LoggerFactory.getLogger(ThreadPoolConfiguration.class);

    /**
     * 系统检查池服务执行
     *
     * @return {@link ThreadPoolExecutor}
     */
    @Bean(name = "defaultThreadPoolExecutor", destroyMethod = "shutdown")
    public ThreadPoolExecutor systemCheckPoolExecutorService() {

        return new ThreadPoolExecutor(3, 10, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(10000),
                new ThreadFactoryBuilder().setNameFormat("default-executor-%d").build(),
                (r, executor) -> log.error("system pool is full! "));
    }
}
