package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQReplyListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * @since 2022/12/24 23:42
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "reply_topic", consumerGroup = "reply_group")
public class ReplyConsumer implements RocketMQReplyListener<String, byte[]> {
    @Override
    public byte[] onMessage(String s) {
        log.info("接受到消息: {}", s);
        // 返回消息到生成者
        return "返回消息到生产者".getBytes();
    }
}
