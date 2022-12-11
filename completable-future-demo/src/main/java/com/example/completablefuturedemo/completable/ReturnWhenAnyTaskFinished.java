package com.example.completablefuturedemo.completable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Real
 * @since 2022/12/11 0:23
 */
public class ReturnWhenAnyTaskFinished {

    public void implementByCompletableFuture() {
        // 1. 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Integer> list = Arrays.asList(1, 2, 3);
        long start = System.currentTimeMillis();
        // 2. 提交任务，主要由 AnyOf 方法表示任意方法完成调用
        CompletableFuture<Object> completableFuture = CompletableFuture.anyOf(list.stream().map(key ->
                CompletableFuture.supplyAsync(() -> {
                    // 睡眠一秒，模仿处理过程
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException ignored) {
                    }
                    return "结果" + key;
                }, executorService))
                .toArray(CompletableFuture[]::new));
        executorService.shutdown();
        long end = System.currentTimeMillis();
        // 3. 获取结果
        System.out.println(completableFuture.join() + "运行时间：" + (end - start));
    }

}
