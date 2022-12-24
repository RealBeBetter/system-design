package com.company.rocketmqdemo.consumer.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * @since 2022/12/24 23:06
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "batch_topic", consumerGroup = "batch_group")
public class BatchConsumer implements RocketMQListener<String> {
    /**
     * 处理接受的消息
     *
     * @param message 消息
     */
    @Override
    public void onMessage(String message) {
        log.info("批量消息-接受到消息:" + message);
    }
}
