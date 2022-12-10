package com.example.completablefuturedemo.completable;

import java.util.concurrent.CompletableFuture;

/**
 * @author Real
 * @since 2022/12/11 0:30
 */
public class EitherDemo {

    public static void implementByCompletableFuture() {
        CompletableFuture<String> meal = CompletableFuture.supplyAsync(() -> {
            return "饭做好了";
        });
        CompletableFuture<String> outMeal = CompletableFuture.supplyAsync(() -> {
            return "外卖到了";
        });

        // 饭先做好，就吃饭。外卖先到，就吃外卖。就是这么任性。
        CompletableFuture<String> completableFuture = meal.applyToEither(outMeal, myMeal -> {
            return myMeal;
        });

        System.out.println(completableFuture.join());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            // 出现两者之一的情况
            implementByCompletableFuture();
        }
    }

}
