package com.company.rabbitmq.deadLetter;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author Real
 * @date 2021年08月29日 10:48
 */
public class Producer {

    // 普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    // 死信队列生产者
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        // 设置死信消息 TTL时间 单位毫秒值
        /*AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();*/

        for (int i = 0; i < 10; i++) {
            String message = "info" + i;    // info0 ... info9
            // 发布消息
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes(StandardCharsets.UTF_8));
        }
    }

}
