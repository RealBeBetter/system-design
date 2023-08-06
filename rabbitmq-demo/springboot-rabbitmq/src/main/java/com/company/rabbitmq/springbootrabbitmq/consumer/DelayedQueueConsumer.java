package com.company.rabbitmq.springbootrabbitmq.consumer;

import com.company.rabbitmq.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @ author： Real
 * @ date： 2021年08月30日 14:09
 * 延迟队列的消费者，基于插件的延迟队列
 */
@Slf4j
@Component
public class DelayedQueueConsumer {

    /**
     * 接收延迟队列，监听消息，接收消息
     *
     * @param message 消息
     */
    @RabbitListener(queues = "delayed.queue")
    public void receiveDelayQueue(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到延迟队列的消息：{}", new Date(), msg);
    }
}
