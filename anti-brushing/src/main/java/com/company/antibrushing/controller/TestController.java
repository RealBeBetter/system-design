package com.company.antibrushing.controller;

import com.company.antibrushing.annotation.Prevent;
import com.company.antibrushing.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Real
 * @since 2022/12/9 0:52
 */
@RestController
public class TestController {

    /**
     * 测试防刷
     * 加上该注解即可实现短信防刷(默认一分钟内不允许重复调用，支持扩展、配置）
     *
     * @param request 请求
     * @return 返回
     */
    @GetMapping(value = "/testPrevent/{request}")
    @Prevent
    public ApiResponse testPrevent(@PathVariable("request") String request) {
        return ApiResponse.success("调用成功");
    }


    /**
     * 测试防刷
     *
     * @param request 请求
     * @return 返回
     */
    @GetMapping(value = "/testPreventIncludeMessage/{request}")
    @Prevent(message = "10秒内不允许重复调多次", value = "10")//value 表示10表示10秒
    public ApiResponse testPreventIncludeMessage(@PathVariable("request") String request) {
        return ApiResponse.success("调用成功");
    }
}
