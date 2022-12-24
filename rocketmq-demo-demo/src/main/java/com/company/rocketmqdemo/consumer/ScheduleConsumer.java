package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Real
 * Date: 2022/9/9 0:40
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = "scheduled_topic", consumerGroup = "scheduled_group")
public class ScheduleConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("延时消息-接收到消息: {}, 时间: {}", s, sdf.format(new Date()));
    }
}
