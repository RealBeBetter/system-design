package com.company.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ author： Real
 * @ date： 2021年08月02日 10:20
 * 连接工厂的工具类
 */
public class RabbitMQUtil {
    // 获取连接信道
    public static Channel getChannel() throws IOException, TimeoutException {
        // 创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置主机IP
        factory.setHost("192.168.111.138");
        // 设置用户名
        factory.setUsername("admin");
        // 设置密码
        factory.setPassword("123");
        // 建立连接
        Connection connection = factory.newConnection();
        // 获取信道
        return connection.createChannel();
    }
}
