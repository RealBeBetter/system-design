package com.example.completablefuturedemo.completable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Real
 * @since 2022/12/11 0:15
 */
public class WaitAllTaskComplete {

    public static void implementByCompletableFuture() {
        // 1. 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Integer> list = Arrays.asList(1, 2, 3);
        // 2. 提交任务，并调用join()阻塞等待所有任务执行完成
        CompletableFuture.allOf(list.stream().map(key ->
                CompletableFuture.runAsync(() -> {
                    // 睡眠一秒，模仿处理过程
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignored) {
                    }
                    System.out.println("结果" + key);
                }, executorService))
                .toArray(CompletableFuture[]::new))
                .join();
        executorService.shutdown();
    }

    public static void implementByThread() {
        // 1. 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Integer> list = Arrays.asList(1, 2, 3);
        CountDownLatch countDownLatch = new CountDownLatch(list.size());
        for (Integer key : list) {
            // 2. 提交任务
            executorService.execute(() -> {
                // 睡眠一秒，模仿处理过程
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ignored) {
                }
                System.out.println("结果" + key);
                countDownLatch.countDown();
            });
        }

        executorService.shutdown();
        // 3. 阻塞等待所有任务执行完成
        try {
            countDownLatch.await();
        } catch (InterruptedException ignored) {
        }
    }

    public static void main(String[] args) {
        implementByThread();
        System.out.println();
        implementByCompletableFuture();
    }

}
