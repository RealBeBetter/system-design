package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.TestProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Real
 * Date: 2022/9/8 0:35
 */
@RestController
public class TestController {

    @Autowired
    private TestProducer testProducer;

    @GetMapping("/test")
    public String sendMessage() {
        testProducer.sendMessage();
        return "测试消息案例";
    }

}
