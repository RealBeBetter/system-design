package com.company.rabbitmq.springbootrabbitmq.controller;

import com.company.rabbitmq.springbootrabbitmq.config.PublishConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ author： Real
 * @ date： 2021年08月30日 18:02
 * 发布确认测试，生产者
 */
@Slf4j
@RestController
@RequestMapping("/confirm")
public class PublishConfirmController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 发消息
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {

        /*CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE_NAME,
                PublishConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData1);
        log.info("发送消息内容：{}", message);*/

        /*CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE_NAME + "123",
                PublishConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData2);
        log.info("发送消息内容：{}", message);*/

        CorrelationData correlationData3 = new CorrelationData("3");
        rabbitTemplate.convertAndSend(PublishConfirmConfig.CONFIRM_EXCHANGE_NAME,
                PublishConfirmConfig.CONFIRM_ROUTING_KEY + "123", message, correlationData3);
        log.info("发送消息内容：{}", message);
    }

}
