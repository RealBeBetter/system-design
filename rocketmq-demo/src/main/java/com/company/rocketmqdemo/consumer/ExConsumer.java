package com.company.rocketmqdemo.consumer;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * @since 2022/12/24 23:52
 */
@Component
@RocketMQMessageListener(topic = "topicName", consumerGroup = "groupName")
public class ExConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {

    }
}
