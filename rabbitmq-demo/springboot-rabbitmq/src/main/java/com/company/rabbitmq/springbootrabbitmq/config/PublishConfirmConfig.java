package com.company.rabbitmq.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 发布确认配置类
 *
 * @author wei.song
 * @date 2021年08月30日 17:49
 */
@Configuration
public class PublishConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    public static final String CONFIRM_ROUTING_KEY = "key1";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    @Bean
    public DirectExchange confirmExchange() {
        // 将无法投递的消息能够转发给备份交换机的关联操作
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME).build();
    }

    @Bean
    public Queue confirmQueue() {
        return new Queue(CONFIRM_QUEUE_NAME);
    }

    @Bean
    public Binding queueBindingExchange(
            @Qualifier("confirmExchange") DirectExchange exchange,
            @Qualifier("confirmQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }

    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    @Bean
    public Queue backupQueue() {
        return new Queue(BACKUP_QUEUE_NAME);
    }

    @Bean
    public Queue warningQueue() {
        return new Queue(WARNING_QUEUE_NAME);
    }

    @Bean
    public Binding backupQueueBindingBackupExchange(
            @Qualifier("backupExchange") FanoutExchange exchange,
            @Qualifier("backupQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding warningQueueBindingBackupExchange(
            @Qualifier("backupExchange") FanoutExchange exchange,
            @Qualifier("warningQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange);
    }

}
