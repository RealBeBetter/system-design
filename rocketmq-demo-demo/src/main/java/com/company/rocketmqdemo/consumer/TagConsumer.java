package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * Date: 2022/10/19 0:09
 */
@Slf4j
@Component
@RocketMQMessageListener(selectorExpression = "TAG-A||TAG-B", topic = "tag_topic", consumerGroup = "tag_group")
public class TagConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("标签过滤消息-接受到消息:" + message);
    }
}
