package com.company.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ author： Real
 * @ date： 2021年08月02日 9:24
 * 生产者： 发送消息
 */
public class Producer {

    // 队列名称
    public static final String QUEUE_NAME = "hello";

    // 发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 工厂IP，连接RabbitMQ的队列
        factory.setHost("192.168.111.132");
        // 用户名
        factory.setUsername("admin");
        // 密码
        factory.setPassword("123");

        // 创建连接
        Connection connection = factory.newConnection();
        // 获取连接之后，发送消息需要通过信道才能完成
        // 获取信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * 1.队列名称；
         * 2.队列是否持久化，默认不持久化保存在内存中，持久化保存在磁盘中
         * 3.是否排他，默认为false（是否只供一个消费者进行消费，是否进行消息共享，true则可以共享）
         * 4.是否自动删除，最后一个消费者端断开连接之后，如果是true表示自动删除，默认是false
         * 5.设置队列的其他一些参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发消息
        String message = "Hello World";

        /**
         * 发送一个消息
         * 1.发送到哪个交换机
         * 2.路由的Key值，本次是队列名称
         * 3.其他参数信息
         * 4.发送消息的消息体
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }

}
