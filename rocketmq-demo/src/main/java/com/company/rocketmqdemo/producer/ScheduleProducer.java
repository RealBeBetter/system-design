package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Real
 * Date: 2022/9/9 0:42
 */
@Slf4j
@Service
public class ScheduleProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void scheduled() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String text = "延时消息：" + sdf.format(new Date());
        log.info("延迟消息开始发送，时间：{}", sdf.format(new Date()));
        // 设置延时等级2，这个消息将在5s之后发送
        // 1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        Message<String> message = MessageBuilder.withPayload(text).build();
        rocketMQTemplate.syncSend("scheduled_topic", message, 1000, 2);
        log.info("延迟消息发送结束，时间：{}", sdf.format(new Date()));
    }

}
