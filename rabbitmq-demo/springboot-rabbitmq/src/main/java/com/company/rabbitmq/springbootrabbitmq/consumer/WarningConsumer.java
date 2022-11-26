package com.company.rabbitmq.springbootrabbitmq.consumer;

import com.company.rabbitmq.springbootrabbitmq.config.PublishConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @ author： Real
 * @ date： 2021年08月31日 13:44
 * 警报消费者
 */
@Slf4j
@Component
public class WarningConsumer {

    // 接收警报消息
    @RabbitListener(queues = PublishConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.info("警报消费者接收到不可路由消息：{}", msg);
    }

}
