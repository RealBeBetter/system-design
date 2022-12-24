package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Real
 * Date: 2022/9/8 21:27
 */
@Slf4j
@Service
public class BaseProducer {

    public static final String DESTINATION = "base_topic";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sync() {
        String text = "基本消息案例-同步消息";
        log.info("同步消息开始发送，时间: {}", System.currentTimeMillis());
        rocketMQTemplate.syncSend(DESTINATION, text);
        log.info("同步消息发送完成，消息: {}, 时间: {}", text, System.currentTimeMillis());
    }

    public void async() {
        String text = "基本消息案例-异步消息";
        log.info("异步消息开始发送，时间: {}", System.currentTimeMillis());
        for (int i = 1; i <= 10; i++) {
            // 最后一个参数表示 timeout
            rocketMQTemplate.asyncSend(DESTINATION, text + "ID: " + i, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("异步消息发送完成，消息: {}, 时间: {}", text, System.currentTimeMillis());
                }

                @Override
                public void onException(Throwable throwable) {
                    log.warn("异步消息发送失败，消息: {}, 时间: {}", text, System.currentTimeMillis());
                }
            }, 2000);
        }
        log.info("基本消息案例-异步消息发送结束，时间：{}", System.currentTimeMillis());
    }

    public void oneWay() {
        String text = "基本信息案例-单向发送";
        log.info("单向消息开始发送，时间: {}", System.currentTimeMillis());
        rocketMQTemplate.sendOneWay(DESTINATION, text);
        log.info("单向消息发送完成，消息: {}, 时间: {}", text, System.currentTimeMillis());
    }

}
