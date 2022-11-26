package com.company.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ author： Real
 * @ date： 2021年08月30日 17:49
 * 发布确认配置类
 */
@Configuration
public class PublishConfirmConfig {

    // 交换机
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    // 队列
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    // RoutingKey
    public static final String CONFIRM_ROUTING_KEY = "key1";
    // 备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    // 备份队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    // 报警队列
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    // 声明定义交换机
    @Bean
    public DirectExchange confirmExchange() {
        // 将无法投递的消息能够转发给备份交换机的关联操作
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    // 声明定义队列
    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    // 绑定
    @Bean
    public Binding queueBindingExchange(
            @Qualifier("confirmExchange") DirectExchange exchange,
            @Qualifier("confirmQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }

    // 声明备份交换机
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    // 声明备份队列
    @Bean
    public Queue backupQueue() {
        return new Queue(BACKUP_QUEUE_NAME);
    }

    // 声明警报队列
    @Bean
    public Queue warningQueue() {
        return new Queue(WARNING_QUEUE_NAME);
    }

    // 备份队列绑定备份交换机
    @Bean
    public Binding backupQueueBindingBackupExchange(
            @Qualifier("backupExchange") FanoutExchange exchange,
            @Qualifier("backupQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    // 警报队列绑定备份交换机
    @Bean
    public Binding warningQueueBindingBackupExchange(
            @Qualifier("backupExchange") FanoutExchange exchange,
            @Qualifier("warningQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }


}
