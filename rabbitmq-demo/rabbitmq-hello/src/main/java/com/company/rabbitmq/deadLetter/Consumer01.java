package com.company.rabbitmq.deadLetter;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @ author： Real
 * @ date： 2021年08月17日 10:21
 */
public class Consumer01 {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    // 死信交换机名称
    public static final String DEAD_EXCHANGE = "dead_exchange";
    // 普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    // 死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        // 声明死信队列和普通队列交换机的类型为direct交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        // 设置arguments所需要的参数
        Map<String, Object> arguments = new HashMap<>();
        // 设置过期时间，单位ms，通常也可以由生产者指定时间
        // arguments.put("x-message-ttl", 10000);
        // 给正常队列设置死信交换机
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        // 给正常队列设置死信RoutingKey
        arguments.put("x-dead-letter-routing-key", "lisi");
        // 给正常队列设置限制消息数
        // arguments.put("x-max-length", 6);

        // 声明普通队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        // 声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定普通交换机和普通队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        // 绑定死信交换机和死信队列
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("等待接收消息...");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), StandardCharsets.UTF_8);
            if ("info5".equals(msg)) {
                System.out.println("Consumer1接受的消息是：" + msg + "：此消息被C1拒绝");
                // 拒绝消息，获取被拒绝的消息的Tag标签，然后决定是否放回队列，此处选择不放回原有队列，最终将消息转发到死信队列
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer1接受的消息是：" + msg);
                // 应答消息，获取被应答的消息的Tag标签，然后决定是否批量应答，此处选择不批量应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };
        // 消费者消费消息，开启手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });
    }
}
