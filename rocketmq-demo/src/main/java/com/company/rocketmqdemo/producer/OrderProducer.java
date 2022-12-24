package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author Real
 * Date: 2022/9/8 22:48
 */
@Slf4j
@Service
public class OrderProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendOrder() {
        log.info("顺序消息");
        try {
            for (int i = 1; i <= 10; i++) {
                int num = (int) (Math.random() * 10000);
                // 设置一个延时，表示同一个消息先后进入到 Queue 中
                TimeUnit.MILLISECONDS.sleep(100);
                log.info("顺序消息，ID:" + num);
                // 第一个参数为 topic，第二个参数为内容，第三个参数为 Hash 值，不同 hash 值在不同的队列中
                rocketMQTemplate.syncSendOrderly("order_topic", "顺序消息，ID:" + num, "order");
            }
            log.info("顺序消息已发送...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
