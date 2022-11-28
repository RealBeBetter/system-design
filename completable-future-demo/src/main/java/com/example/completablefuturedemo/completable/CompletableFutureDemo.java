package com.example.completablefuturedemo.completable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * https://mp.weixin.qq.com/s/WIhS5E1LbyC6muAMwj4UxQ
 *
 * @author Real
 * Date: 2022/11/28 20:40
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        // 创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        List<Integer> list = Arrays.asList(1, 2, 3);
        for (Integer key : list) {
            CompletableFuture.supplyAsync(() -> {
                try {
                    long time = new Random().nextInt(1000);
                    Thread.sleep(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "result: " + key;
            }, threadPool).whenCompleteAsync((result, exception) -> {
                // 获取结果
                System.out.println(result);
            });
        }
        threadPool.shutdown();

        // 由于whenCompleteAsync获取结果的方法是异步的，所以要阻塞当前线程才能输出结果
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
