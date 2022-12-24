package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * Date: 2022/9/8 22:46
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "order_topic", consumerGroup = "order_group", consumeMode = ConsumeMode.ORDERLY)
public class OrderConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("顺序消息生产-接收到消息:" + message);
    }

}
