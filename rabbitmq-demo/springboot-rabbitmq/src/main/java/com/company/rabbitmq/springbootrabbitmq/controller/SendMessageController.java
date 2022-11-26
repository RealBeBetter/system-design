package com.company.rabbitmq.springbootrabbitmq.controller;

import com.company.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ author： Real
 * @ date： 2021年08月29日 13:46
 * 发送消息  http://localhost:8080/ttl/sendMsg/嘻嘻嘻
 */
@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{}，发送一条消息给TTL队列：{}", new Date(), message);
        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列：" + message);
    }

    // 开始发消息 消息 TTL
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间：{}，发送一条时长为{}毫秒消息给QC：{}", new Date(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            // 设置发送消息的时候的发送时长 延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    // 发送消息，基于延迟插件的消息以及延迟时间
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info("当前时间：{}，发送一条延迟时长为{}毫秒消息给延迟队列delayed.queue：{}", new Date(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME, DelayedQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            // 发送延迟消息，设置延迟时间，ms
            msg.getMessageProperties().setDelay(delayTime);
            return msg;
        });
    }

}
