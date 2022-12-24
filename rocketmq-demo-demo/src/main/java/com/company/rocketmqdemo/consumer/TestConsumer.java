package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * Date: 2022/9/8 0:32
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "test_topic", consumerGroup = "test_group")
public class TestConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("ConsumerTest - 接收到消息:" + s);
    }

}
