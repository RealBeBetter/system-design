package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Real
 * @since 2022/12/24 23:41
 */
@Slf4j
@Service
public class ReplyProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送应答消息
     */
    public void sendReplyMessage() {

        // 如果消费者没有回馈消息，则不会发送下一条消息
        for (int i = 1; i <= 10; i++) {
            String text = "回馈消息" + "--" + i;
            log.info("发送：{}", text);
            Object obj = rocketMQTemplate.sendAndReceive("reply_topic", text, String.class);
            log.info("消费者返回的消息：" + obj);
        }
    }
}
