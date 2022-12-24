package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.batch.BatchProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Real
 * @since 2022/12/24 23:10
 */
@RestController
public class BatchController {

    /**
     * 批量消息发送
     */
    @Resource
    private BatchProducer batchProducer;

    @GetMapping("/batch")
    public Object batch() {
        // 批量消息样例
        batchProducer.batchSendMessage();
        return "批量消息样例";
    }

}
