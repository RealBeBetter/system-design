package com.company.rabbitmq.manualAnswer;

import com.company.rabbitmq.utils.RabbitMQUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * @ author： Real
 * @ date： 2021年08月02日 14:44
 * 消息在手动应答的时候应该是不丢失的，会重新放到消息队列中等待重新消费
 */
public class Task02 {

    // 队列名称
    public static final String TASK_QUEUE_NAME = "ack_queue";

    // 发送消息
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = RabbitMQUtil.getChannel();

        // 发布确认
        channel.confirmSelect();

        // 队列声明
        boolean durable = true; // 队列持久化
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
        // 从控制台输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            // 发布消息
            // 设置生产者发送的消息为持久化消息（消息需要保存到磁盘）默认一般是保存在内存中
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println("生产者发送消息：" + message);
        }
    }

}
