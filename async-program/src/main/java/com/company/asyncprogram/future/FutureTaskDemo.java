package com.company.asyncprogram.future;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author Real
 * @since 2022/12/9 1:40
 */
@Slf4j
public class FutureTaskDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        // FutureTask包装 callable 任务，再交给线程池执行
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            System.out.println("子线程开始计算：");
            int sum = 0;
            for (int i = 1; i <= 100; i++) {
                sum += i;
            }
            return sum;
        });

        // 线程池执行任务， 运行结果在 futureTask 对象里面
        executor.submit(futureTask);

        try {
            System.out.println("task运行结果计算的总和为：" + futureTask.get());
        } catch (Exception e) {
            log.error("error occurred, errorMsg = {}", e.getMessage());
        }
        executor.shutdown();
    }
}
