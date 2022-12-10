package com.example.completablefuturedemo.completable;

import java.util.concurrent.CompletableFuture;

/**
 * @author Real
 * @since 2022/12/11 0:29
 */
public class CompleteDemo {

    public static void implementByCompletableFuture() {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            return "饭做好了";
        });

        try {
            Thread.sleep(1L);
        } catch (InterruptedException ignored) {
        }

        completableFuture.complete("饭还没做好，我点外卖了");
        System.out.println(completableFuture.join());
    }

    public static void main(String[] args) {
        implementByCompletableFuture();
    }

}
