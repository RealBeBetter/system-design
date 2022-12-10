package com.example.completablefuturedemo.completable;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * @author Real
 * @since 2022/12/11 0:27
 */
public class ThenHandlerDemo {

    static Random random = new Random();

    public static void implementByCompletableFuture() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println("1. 开始淘米");
            return "2. 淘米完成";
        }).thenApplyAsync(result -> {
            System.out.println(result);
            System.out.println("3. 开始煮饭");
            // 生成一个1~10的随机数
            if (random.nextInt(10) + 1 > 5) {
                throw new RuntimeException("4. 电饭煲坏了，煮不了");
            }
            return "4. 煮饭完成";
        }).handleAsync((result, exception) -> {
            if (exception != null) {
                System.out.println(exception.getMessage());
                return "5. 今天没饭吃";
            } else {
                System.out.println(result);
                return "5. 开始吃饭";
            }
        });

        try {
            String result = completableFuture.get();
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        implementByCompletableFuture();
    }

}
