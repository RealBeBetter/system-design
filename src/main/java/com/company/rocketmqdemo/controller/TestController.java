package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.ProducerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Real
 * Date: 2022/9/8 0:35
 */
@RestController
public class TestController {

    @Autowired
    private ProducerTest producerTest;

    @RequestMapping("/test")
    public void sendMessage() {
        producerTest.sendMessage();
    }

}
