package com.company.asyncprogram.completable;

import java.util.concurrent.CompletableFuture;

/**
 * @author Real
 * @since 2022/12/9 1:41
 */
public class CompletableFutureDemo {

    public static void main(String[] args) {
        //任务1：洗水壶->烧开水
        CompletableFuture<Void> f1 =
                CompletableFuture.runAsync(() -> {
                    System.out.println("T1:洗水壶...");
                    try {
                        Thread.sleep(1);
                        System.out.println("T1:烧开水...");
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

        //任务2：洗茶壶->洗茶杯->拿茶叶
        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    System.out.println("T2:洗茶壶...");
                    try {
                        Thread.sleep(1);
                        System.out.println("T2:洗茶杯...");
                        Thread.sleep(2);

                        System.out.println("T2:拿茶叶...");
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "龙井";
                });

        //任务3：任务1和任务2完成后执行：泡茶
        CompletableFuture<String> f3 =
                f1.thenCombine(f2, (__, tf) -> {
                    System.out.println("T1:拿到茶叶:" + tf);
                    System.out.println("T1:泡茶...");
                    return "上茶:" + tf;
                });

        //等待任务3执行结果
        System.out.println(f3.join());

    }

}
