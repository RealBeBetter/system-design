package com.example.completablefuturedemo.completable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 接力完成任务
 *
 * @author Real
 * @since 2022/12/11 0:25
 */
public class RelayToCompleteTask {

    public static void main(String[] args) {
        implementByCompletableFuture();
    }

    public static void implementByCompletableFuture() {
        // 1. 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 2. 提交任务，并调用join()阻塞等待任务执行完成
        String result2 = CompletableFuture.supplyAsync(() -> {
            // 睡眠一秒，模仿处理过程
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }
            return "结果1";
        }, executorService).thenApplyAsync(result1 -> {
            // 睡眠一秒，模仿处理过程
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException ignored) {
            }
            return result1 + "结果2";
        }, executorService).join();

        executorService.shutdown();
        // 3. 获取结果
        System.out.println(result2);
    }

}
