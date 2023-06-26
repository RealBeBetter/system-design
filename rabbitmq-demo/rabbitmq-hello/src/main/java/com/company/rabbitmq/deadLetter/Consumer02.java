package com.company.rabbitmq.deadLetter;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Real
 * @date 2021年08月29日 11:05
 */
public class Consumer02 {

    // 死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer2接受的消息是：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        // 消费者消费消息
        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });
    }

}
