package com.company.asyncprogram.thread;

/**
 * @author Real
 * @since 2022/12/9 1:31
 */
public class AsyncThread extends Thread {
    @Override
    public void run() {
        System.out.println("当前线程名称:" + this.getName() + ", 执行线程名称:" + Thread.currentThread().getName() + "-hello");
    }
}
