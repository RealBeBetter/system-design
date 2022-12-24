package com.company.rocketmqdemo.controller;

import com.company.rocketmqdemo.producer.TagProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Real
 * Date: 2022/10/19 0:12
 */
@Slf4j
@RestController
public class TagController {

    /**
     * 标签过滤消息
     */
    @Resource
    private TagProducer tagProducer;

    @GetMapping("/tag")
    public Object tag() {
        // TAG过滤
        tagProducer.tag();
        return "指定标签消息";
    }

}
