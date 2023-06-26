package com.example.commonutil.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wei.song
 * @since 2023/6/26 19:24
 */
@Slf4j
public class ExecutorUtil {

    public static final ThreadPoolExecutor COMMON_POOL = initPool(Runtime.getRuntime().availableProcessors() * 2, "default");

    public static ScheduledThreadPoolExecutor newScheduledThreadPool(int corePoolSize) {
        return newScheduledThreadPool(corePoolSize, getCallerClassName());
    }

    private static ScheduledThreadPoolExecutor newScheduledThreadPool(int corePoolSize, String threadFactoryNamePrefix) {
        return new ScheduledThreadPoolExecutor(corePoolSize, new DefaultThreadFactory(threadFactoryNamePrefix));
    }

    private static String getCallerClassName() {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        for (int idx = stacks.length - 1; idx >= 0; idx--) {
            if (stacks[idx].getClassName().equals(ExecutorUtil.class.getName())) {
                int caller = idx + 1;
                if (caller < stacks.length) {
                    return StringUtils.substringBefore(stacks[caller].getFileName(), ".");
                }
            }
        }
        return "UNKNOWN";
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String prefix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = StringUtils.isBlank(prefix) ? "pool-" + POOL_NUMBER.getAndIncrement() + "-thread-"
                    : "pool-" + prefix + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(@NotNull Runnable runnable) {
            Thread thread = new Thread(group, runnable, namePrefix + threadNumber.getAndIncrement(), 0);
            if (thread.isDaemon()) {
                thread.setDaemon(false);
            }
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    }

    public static ThreadPoolExecutor initPool(int corePoolSize) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new DefaultThreadFactory(getCallerClassName()),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static ThreadPoolExecutor initPool(int corePoolSize, String threadName) {
        return new ThreadPoolExecutor(corePoolSize, corePoolSize, 10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new DefaultThreadFactory(threadName),
                new ThreadPoolExecutor.AbortPolicy());
    }

    public static void waitForCompleted(ExecutorService pool) {
        waitForCompleted(pool, 60);
    }

    public static void waitForCompleted(ExecutorService pool, long minutes) {
        pool.shutdown();
        try {
            boolean await = pool.awaitTermination(minutes, TimeUnit.MINUTES);
            log.info("await completed: {}", await);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("execute interrupted", e);
        }
    }

}
