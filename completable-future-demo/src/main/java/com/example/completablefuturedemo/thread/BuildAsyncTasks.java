package com.example.completablefuturedemo.thread;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Real
 * @since 2023/2/12 14:41
 */
@Slf4j
public class BuildAsyncTasks {

    private final ThreadPoolExecutor poolExecutor = initPool(
            Runtime.getRuntime().availableProcessors() * 2,
            Runtime.getRuntime().availableProcessors() * 2,
            "completable-future-task");

    public static void main(String[] args) {
        List<List<Integer>> allList = Lists.newArrayList();

        allList.add(Lists.newArrayList(1, 2, 3));
        allList.add(Lists.newArrayList(4, 5, 6));
        allList.add(Lists.newArrayList(7, 8, 9));

        BuildAsyncTasks test = new BuildAsyncTasks();

        List<Supplier<Integer>> suppliers = test.buildTask(allList);
        System.out.println(test.executeAsync(suppliers));
    }

    private List<Integer> executeAsync(List<Supplier<Integer>> suppliers) {
        List<CompletableFuture<Integer>> completableFutures = suppliers.stream()
                .map(supplier -> CompletableFuture.supplyAsync(supplier, poolExecutor))
                .collect(Collectors.toList());

        CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0])).join();

        return completableFutures.stream().map(completableFuture -> {
            try {
                return completableFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("execute task error:", e);
            }
            return 0;
        }).collect(Collectors.toList());
    }

    private List<Supplier<Integer>> buildTask(List<List<Integer>> lists) {
        List<Supplier<Integer>> suppliers = new ArrayList<>();
        lists.forEach(list -> suppliers.add(() -> findFirstNumber(list)));
        return suppliers;
    }

    private Integer findFirstNumber(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("current list is empty");
        }

        return list.stream().findFirst().orElse(0);
    }

    public static ThreadPoolExecutor initPool(int corePoolSize, int maximumPoolSize, String threadName) {
        return new ThreadPoolExecutor(
                corePoolSize, maximumPoolSize, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(100),
                new DefaultThreadFactory(threadName), new ThreadPoolExecutor.AbortPolicy());
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger();
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory(String prefix) {
            SecurityManager securityManager = System.getSecurityManager();
            group = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = StringUtils.isEmpty(prefix) ? "pool-" + POOL_NUMBER.getAndIncrement() + "-thread-"
                    : "pool-" + prefix + "-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(@Nonnull Runnable runnable) {
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
}
