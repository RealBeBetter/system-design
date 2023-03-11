package com.example.crawlordermultithread.util;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wei.song
 * @since 2023/2/15 21:36
 */
public class ThreadPoolUtil {

    public static ThreadPoolExecutor initPool(int corePoolSize, int maximumPoolSize, String threadName) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100),
                new DefaultThreadFactory(threadName), new ThreadPoolExecutor.AbortPolicy());
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup GROUP;
        private final AtomicInteger THREAD_NUMBER = new AtomicInteger(1);
        private final String NAME_SUFFIX;

        DefaultThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            GROUP = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            NAME_SUFFIX = StringUtils.isBlank(prefix) ? "pool-" + POOL_NUMBER.getAndIncrement() + "-thread-"
                    : "pool-" + prefix + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(@NotNull Runnable runnable) {
            Thread thread = new Thread(GROUP, runnable, NAME_SUFFIX + THREAD_NUMBER.getAndIncrement(), 0);
            // 设置为非守护线程，在主线程关闭的时候，线程池不会关闭
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }

}
