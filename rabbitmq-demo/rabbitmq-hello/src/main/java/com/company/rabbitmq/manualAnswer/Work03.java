package com.company.rabbitmq.manualAnswer;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息在手动应答的时候应该是不丢失的，会重新放到消息队列中等待重新消费
 *
 * @author wei.song
 * @date 2021年08月02日 14:52
 */
public class Work03 {

    /**
     * 任务队列名称
     */
    public static final String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 接收消息
        Channel channel = RabbitMQUtil.getChannel();
        System.out.println("C2等待接收消息处理时间较长...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            // 模拟线程响应时间
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接收到的消息：" + new String(message.getBody()));

            /**
             * 手动应答
             * 1.消息的标识 tag
             * 2.是否批量应答 false不批量应答Channel中的消息
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        // 开启不公平分发
        // int prefetchCount = 1;
        // 预取值 5
        int prefetchCount = 5;
        channel.basicQos(prefetchCount);

        // 采用手动应答
        boolean autoAck = false;
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, ((consumerTag) -> {
            System.out.println(consumerTag + "消费者取消消费接口回调逻辑");
        }));
    }

}
