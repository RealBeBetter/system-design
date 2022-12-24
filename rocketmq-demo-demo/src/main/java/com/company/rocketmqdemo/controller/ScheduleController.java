package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.ScheduleProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Real
 * Date: 2022/9/9 0:43
 */
@Slf4j
@RestController
public class ScheduleController {

    @Autowired
    private ScheduleProducer scheduleProducer;

    @GetMapping("/schedule")
    public Object scheduled() {
        scheduleProducer.scheduled();
        return "发送延时消息";
    }
}
