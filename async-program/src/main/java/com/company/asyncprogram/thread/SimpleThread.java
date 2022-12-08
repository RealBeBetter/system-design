package com.company.asyncprogram.thread;

/**
 * @author Real
 * @since 2022/12/9 1:30
 */
public class SimpleThread {

    public static void main(String[] args) {
        // 模拟业务流程
        // .......
        // 创建异步线程
        AsyncThread asyncThread = new AsyncThread();
        // 启动异步线程
        asyncThread.start();
    }

}
