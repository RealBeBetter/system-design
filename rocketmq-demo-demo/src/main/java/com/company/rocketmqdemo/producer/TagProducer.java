package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Real
 * Date: 2022/10/19 0:11
 */
@Slf4j
@Service
public class TagProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    public void tag() {
        String text = "标签过滤消息," + System.currentTimeMillis();
        log.info(text);

        // 任何类型的send方法均可以指定TAG，默认可以不指定则为*
        Message<String> message = MessageBuilder.withPayload(text).build();
        rocketMQTemplate.syncSend("tag_topic:TAG-A", message);
        log.info("已发送...");
    }
}
