package controller;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

/**
 * @author： 雨下一整晚Real
 * @date： 2021年06月14日 19:56
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /*
     * 1. 从Zookeeper注册中心获取到userService的URL
     * 2. 进行远程调用RPC
     * 3. 将结果封装为一个远程的代理对象。给变量赋值
     * */

    /**
     * 远程注入 UserService
     */
    @Reference
    private UserService userService;

    @RequestMapping("/sayHello")
    public String sayHello() {
        return userService.sayHello();
    }

}
