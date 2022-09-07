package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * @author Real
 * Date: 2022/9/8 0:26
 */
@Slf4j
@Service
public class ProducerTest {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMessage() {
        String text = "Hello RocketMQ!";
        Message<String> message = MessageBuilder.withPayload(text).build();
        long start = System.currentTimeMillis();
        log.info("开始发送......");
        rocketMQTemplate.send("test_topic", message);
        long end = System.currentTimeMillis();
        log.info("发送完毕，耗时：{} ms", end - start);
    }
}
