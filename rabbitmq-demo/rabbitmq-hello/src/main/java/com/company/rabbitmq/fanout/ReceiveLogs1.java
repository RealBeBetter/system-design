package com.company.rabbitmq.fanout;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @ author： Real
 * @ date： 2021年08月05日 14:17
 * Fanout接收消息
 */
public class ReceiveLogs1 {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();
        // 声明一个交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        // 声明一个临时队列
        /**
         * 临时队列，消费者断开与队列的连接，临时队列就会自动删除
         * 产生的临时队列名称都是随机的
         */
        String QUEUE_NAME = channel.queueDeclare().getQueue();

        // 队列绑定交换机
        /**
         * 1.目的队列，2.源交换机，3.RoutingKey
         */
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        System.out.println("等待接收消息，将消息打印在控制台上......");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs1接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

}
