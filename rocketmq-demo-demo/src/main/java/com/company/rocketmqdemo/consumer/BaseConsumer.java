package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * Date: 2022/9/8 21:27
 */
@Slf4j
@Component
@RocketMQMessageListener(selectorExpression = "", topic = "base_topic", consumerGroup = "base_group")
public class BaseConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("基本消息案例-接收到消息：{}", s);
    }
}
