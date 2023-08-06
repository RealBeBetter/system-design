package com.company.rabbitmq.springbootrabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 发布确认的回调接口
 *
 * @author wei.song
 * @date 2021年08月31日 11:23
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 交换机确认回调方法
     *
     * @param correlationData
     * @param ack
     * @param cause           1.发消息交换机接收到了回调
     *                        1.1 correlationData 保存回调消息的ID及相关信息
     *                        1.2 交换机收到消息，ack = true
     *                        1.3 cause null
     *                        2.发消息交换机接收失败了回调
     *                        2.1 correlationData 保存回调消息的ID及相关信息
     *                        2.2 交换机收到消息
     *                        2.3 cause 失败的原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String ID = correlationData != null ? correlationData.getId() : "";
        if (ack) {
            log.info("交换机已经收到了ID为：{}的消息", ID);
        } else {
            log.info("交换机未收到ID为：{}的消息；由于原因：{}", ID, cause);
        }
    }

    /**
     * Returned message callback.
     * 可以在消息传递过程中不可达目的地时将消息回退给生产者
     * 只有在消息不可达的情况下才会回退消息
     *
     * @param message    the returned message.
     * @param replyCode  the reply code.
     * @param replyText  the reply text.
     * @param exchange   the exchange.
     * @param routingKey the routing key.
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("消息：{}，被交换机{}退回。退回原因：{}，路由Key：{}",
                new String(message.getBody()), exchange, replyText, routingKey);
    }

}
