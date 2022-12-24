package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.SqlProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Real
 * @since 2022/12/22 21:22
 */
@RestController
public class SqlController {

    /**
     * SQL92过滤消息
     */
    @Resource
    private SqlProducer sqlProducer;

    @GetMapping("/selector")
    public Object selector() {
        // SQL92过滤
        sqlProducer.selector();
        return "过滤消息样例";
    }

}
