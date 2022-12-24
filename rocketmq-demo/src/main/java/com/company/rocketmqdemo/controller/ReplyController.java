package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.ReplyProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 回馈消息样例
 *
 * @author Real
 * @since 2022/12/24 23:43
 */
@RestController
public class ReplyController {
    @Resource
    private ReplyProducer replyProducer;

    @GetMapping("/reply")
    public Object reply() {
        // 消息事务
        replyProducer.sendReplyMessage();
        return "回馈消息样例";
    }
}
