package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.OrderProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Real
 * Date: 2022/9/8 22:50
 */
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderProducer orderProducer;

    @GetMapping("/order")
    public String sendOrder() {
        orderProducer.sendOrder();
        return "顺序消息案例";
    }

}
