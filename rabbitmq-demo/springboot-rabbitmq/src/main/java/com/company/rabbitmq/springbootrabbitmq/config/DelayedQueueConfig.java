package com.company.rabbitmq.springbootrabbitmq.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ author： Real
 * @ date： 2021年08月30日 13:39
 * 延迟队列配置类
 */
@Configuration
public class DelayedQueueConfig {

    // 交换机
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    // 队列
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    // Routing Key
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

    // 声明交换机，延迟交换机需要自定义，基于插件
    @Bean
    public CustomExchange delayedExchange() {
        /**
         * 1.String name        交换机名称
         * 2.String type        交换机类型
         * 3.boolean durable    是否持久化
         * 4.boolean autoDelete 是否自动删除
         * 5.Map<String, Object> arguments  交换机参数
         */
        Map<String, Object> arguments = new HashMap<>();
        // 延迟类型为直接类型
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME,
                "x-delayed-message", false, false, arguments);
    }

    // 声明队列
    @Bean
    public Queue delayedQueue() {
        /**
         * 1.String name        队列名称
         * 2.boolean durable    是否持久化
         * 3.boolean exclusive  是否排他
         * 4.boolean autoDelete 是否自动删除
         * 5.@Nullable Map<String, Object> arguments    其他参数
         */
        return new Queue(DELAYED_QUEUE_NAME);
    }

    // 绑定
    @Bean
    public Binding delayedQueueBindingDelayedExchange(
            @Qualifier("delayedExchange") CustomExchange delayedExchange,
            @Qualifier("delayedQueue") Queue delayedQueue) {
        // 此处需要调用一个构建方法，因为延迟交换机的类型，所以与之前有所不同
        return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
    }
}
