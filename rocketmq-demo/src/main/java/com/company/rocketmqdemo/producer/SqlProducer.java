package com.company.rocketmqdemo.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Real
 * @since 2022/12/22 21:18
 */
@Slf4j
@Service
public class SqlProducer {

    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 选择器，SQL92过滤消息
     */
    public void sendSqlMessage() {
        String text = "SQL92过滤消息，时间戳：" + System.currentTimeMillis();
        log.info(text);
        Message<String> message = MessageBuilder.withPayload(text).build();
        // 设置参数
        Map<String, Object> map = new HashMap<>(4);
        map.put("a", 2);
        map.put("b", 10);
        rocketMQTemplate.convertAndSend("sql_topic", message, map);
        log.info("SQL92过滤消息已发送...");
    }

}
