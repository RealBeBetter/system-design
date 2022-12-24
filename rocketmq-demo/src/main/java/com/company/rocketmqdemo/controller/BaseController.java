package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.BaseProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Real
 * Date: 2022/9/8 21:29
 */
@Slf4j
@RestController
public class BaseController {

    @Autowired
    private BaseProducer baseProducer;

    @GetMapping("/base")
    public String base() {
        baseProducer.sync();
        baseProducer.async();
        baseProducer.oneWay();

        return "基本消息案例";
    }

}
