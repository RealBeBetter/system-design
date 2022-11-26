package com.company.rabbitmq.springbootrabbitmq.consumer;

import com.company.rabbitmq.springbootrabbitmq.config.PublishConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ author： Real
 * @ date： 2021年08月30日 18:07
 * 发布确认消费者
 */
@Slf4j
@Component
public class PublishConfirmConsumer {

    // 接收消息
    @RabbitListener(queues = PublishConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveConfirmMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("接收到队列confirm.queue的消息：{}", msg);
    }

}