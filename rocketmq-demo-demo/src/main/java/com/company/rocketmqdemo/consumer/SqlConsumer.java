package com.company.rocketmqdemo.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Real
 * @since 2022/12/22 21:16
 */
@Slf4j
@Component
@RocketMQMessageListener(selectorType = SelectorType.SQL92,
        selectorExpression = "a between 0 and 6 or b > 8",
        topic = "sql_topic", consumerGroup = "sql_group")
public class SqlConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String message) {
        log.info("SQL92过滤消息-接受到消息:" + message);
    }

}
