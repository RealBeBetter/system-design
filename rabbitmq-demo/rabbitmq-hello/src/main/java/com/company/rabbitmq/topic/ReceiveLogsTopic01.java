package com.company.rabbitmq.topic;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 声明主题交换机及相关队列
 *
 * @author wei.song
 * @date 2021年08月17日 9:26
 */
public class ReceiveLogsTopic01 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 声明信道
        Channel channel = RabbitMQUtil.getChannel();
        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 声明队列
        String queueName = "Q1";
        channel.queueDeclare(queueName, false, false, false, null);
        // 队列绑定
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");
        System.out.println("C1等待接收消息...");
        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("C1接收到消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
            System.out.println("接收队列：" + queueName + " 绑定键： " + message.getEnvelope().getRoutingKey());
        };
        channel.basicConsume(queueName, true,  deliverCallback, consumerTag -> {});
    }

}
