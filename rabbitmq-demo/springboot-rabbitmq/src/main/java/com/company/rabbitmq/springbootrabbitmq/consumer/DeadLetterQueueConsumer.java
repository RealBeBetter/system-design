package com.company.rabbitmq.springbootrabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 死信队列消费者
 *
 * @author wei.song
 * @date 2021年08月29日 13:58
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {

    /**
     * 接收消息
     *
     * @param message 消息
     */
    @RabbitListener(queues = "QD")
    public void receiveD(Message message) {
        String msg = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("当前时间：{}，收到死信队列的消息：{}", new Date(), msg);
    }

}
