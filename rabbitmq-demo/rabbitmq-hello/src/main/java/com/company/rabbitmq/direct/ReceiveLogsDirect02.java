package com.company.rabbitmq.direct;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author wei.song
 * @date 2021年08月16日 21:09
 */
public class ReceiveLogsDirect02 {

    /**
     * 交换机名字
     */
    public static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 获取信道
        Channel channel = RabbitMQUtil.getChannel();
        // 声明交换机，直接交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 声明一个队列
        channel.queueDeclare("disk", false, false, false, null);
        // 队列绑定，设定routingKey为info
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        // 接收消息
        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02控制台接收到的消息：" + new String(message.getBody(), StandardCharsets.UTF_8));
        };

        // 消费者取消消息时回调接口，接收消息
        channel.basicConsume("disk", true, deliverCallback, consumerTag -> {
        });
    }

}
