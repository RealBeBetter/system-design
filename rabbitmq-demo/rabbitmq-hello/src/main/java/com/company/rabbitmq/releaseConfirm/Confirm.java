package com.company.rabbitmq.releaseConfirm;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认：
 * 1.单个发布确认
 * 2.批量发布确认
 * 3.异步批量确认
 * 比较三者耗费的时间差异
 *
 * @author wei.song
 * @date 2021年08月04日 10:08
 */
public class Confirm {

    public static final int MESSAGE_COUNT = 1000;


    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        // 1. 单个发布确认
        // Confirm.publishSingleMessage(); // 一共发布1000条信息，单独发布确认，花了1010ms
        // 2. 批量发布确认
        // Confirm.publishBatchMessage();  // 一共发布1000条信息，批量发布确认，花了159ms
        // 3. 异步批量确认
        Confirm.publishAsyncMessage();  // 一共发布1000条信息，异步发布确认，花了97ms
        // 一共发布1000条信息，异步发布确认，花了46ms
    }

    /**
     * 单个发布确认
     *
     * @throws IOException          ioexception
     * @throws TimeoutException     超时异常
     * @throws InterruptedException 中断异常
     */
    public static void publishSingleMessage() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();

        // 队列声明
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 开启单个发布确认
        channel.confirmSelect();

        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量发送消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            // 等待确认
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("第" + i + "条消息发布完成");
            }
        }

        // 结束时间
        long end = System.currentTimeMillis();

        System.out.println("一共发布" + MESSAGE_COUNT + "条信息，单独发布确认，花了" + (end - begin) + "ms");

    }

    /**
     * 批量发布确认
     *
     * @throws IOException          ioexception
     * @throws TimeoutException     超时异常
     * @throws InterruptedException 中断异常
     */
    public static void publishBatchMessage() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();

        // 队列声明
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        // 开始时间
        long begin = System.currentTimeMillis();

        // 批量确认的数据大小
        int batchSize = 100;

        // 批量发送消息，批量发布确认
        for (int i = 1; i < MESSAGE_COUNT + 1; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("第" + i + "条消息发布完成");
            // 每进行batchSize个消息的发送就确认
            if (i % batchSize == 0) {
                boolean flag = channel.waitForConfirms();
            }
        }

        // 结束时间
        long end = System.currentTimeMillis();

        System.out.println("一共发布" + MESSAGE_COUNT + "条信息，批量发布确认，花了" + (end - begin) + "ms");

    }

    /**
     * 异步发布确认
     *
     * @throws IOException          ioexception
     * @throws TimeoutException     超时异常
     * @throws InterruptedException 中断异常
     */
    public static void publishAsyncMessage() throws IOException, TimeoutException, InterruptedException {
        Channel channel = RabbitMQUtil.getChannel();

        // 队列声明
        String QUEUE_NAME = UUID.randomUUID().toString();
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 开启发布确认
        channel.confirmSelect();

        /**
         * 线程安全有序的一个哈希表，并且适用于高并发场景
         * 1.轻松地将序号和消息关联起来
         * 2.轻松通过序号删除条目
         * 3.支持高并发（多线程安全）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 消息确认成功，回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
            // 2.删除掉已经确认的消息，剩下的就是未经确认的消息
            if (multiple) {
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("消息发送成功：" + deliveryTag);
        };

        /**
         * 消息确认失败，回调函数
         * 1.消息的标识
         * 2.是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            // 3.打印一下未经确认的消息
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是：" + message + "消息发送失败tag：" + deliveryTag);
        };


        /**
         * 添加监听器，监听哪些消息发送成功了，哪些消息发送失败了
         * 1.监听发送成功的消息
         * 2.监听发送失败的消息
         */
        channel.addConfirmListener(ackCallback, nackCallback);  // 异步通知

        // 开始时间
        long begin = System.currentTimeMillis();
        // 批量发布消息，异步发布确认
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("第" + i + "条消息发布完成");
            // 1.记录下所有要发送的消息：记录下消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);

        }

        // 结束时间
        long end = System.currentTimeMillis();

        System.out.println("一共发布" + MESSAGE_COUNT + "条信息，异步发布确认，花了" + (end - begin) + "ms");

    }

}
