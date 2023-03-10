package com.company.rocketmqdemo.producer.batch;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Real
 * @since 2022/12/24 23:05
 */
@Slf4j
@Service
public class BatchProducer {

    /**
     * 测试发送将参数topic定死，实际开发写入到配置文件
     */
    @Resource
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 批量发送信息
     */
    public void batchSendMessage() {
        String text = "批量消息";
        log.info(text);

        List<Message<String>> messageList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            messageList.add(MessageBuilder.withPayload(text + "--" + i).build());
        }
        log.info("开始发送...");

        //把大的消息分裂成若干个小的消息
        MessageSplitter splitter = new MessageSplitter(messageList);

        while (splitter.hasNext()) {
            List<Message<String>> nextList = splitter.next();
            SendResult result = rocketMQTemplate.syncSend("batch_topic", nextList);
            if (result.getSendStatus() == SendStatus.SEND_OK) {
                log.info("发送批量消息成功!消息ID为:{}", result.getMsgId());
            } else {
                log.info("发送批量消息失败!消息ID为:{},消息状态为:{}", result.getMsgId(), result.getSendStatus());
            }
        }
        log.info("已发送...");
    }
}
