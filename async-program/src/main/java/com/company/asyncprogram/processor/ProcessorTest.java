package com.company.asyncprogram.processor;

/**
 * @author Real
 * Date: 2022/11/19 21:14
 */
public class ProcessorTest {

    public static void main(String[] args) {
        // 16 线程，输出的是当前机器的线程数
        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(processors);
    }

}
