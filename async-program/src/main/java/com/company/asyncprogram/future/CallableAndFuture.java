package com.company.asyncprogram.future;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @author Real
 * @since 2022/12/9 1:38
 */
public class CallableAndFuture {
    public static ExecutorService executorService = new ThreadPoolExecutor(4, 40,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());


    /**
     * 可调用类
     *
     * @author Real
     * @since 2022/12/10 00:47
     */
    static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            return "异步处理，Callable 返回结果";
        }
    }

    public static void main(String[] args) {

        for (int i = 0; i < 5; i++) {
            System.out.println("Main 线程运行中......");
        }

        Future<String> future = executorService.submit(new MyCallable());
        try {
            System.out.println(future.get());
        } catch (Exception e) {
            // do something
        } finally {
            executorService.shutdown();
        }
    }
}
