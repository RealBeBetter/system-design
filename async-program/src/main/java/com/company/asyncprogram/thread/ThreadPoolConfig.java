package com.company.asyncprogram.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Real
 * @since 2022/12/9 1:32
 */
public class ThreadPoolConfig {

    public static final Logger log = LoggerFactory.getLogger(ThreadPoolConfig.class);

    @Bean(name = "executorService")
    public ExecutorService downloadExecutorService() {
        return new ThreadPoolExecutor(20, 40, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2000),
                new ThreadFactoryBuilder().setNameFormat("defaultExecutorService-%d").build(),
                (r, executor) -> log.error("defaultExecutor pool is full! "));
    }
}
