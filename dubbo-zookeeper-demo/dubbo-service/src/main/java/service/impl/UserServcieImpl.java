package service.impl;

import org.apache.dubbo.config.annotation.Service;
import service.UserService;

/**
 * @author： 雨下一整晚Real
 * @date： 2021年06月14日 17:54
 */

@Service
// 将这个类提供的方法（服务）对外发布。将访问的地址IP、端口、路径注册到注册中心
// 导入的是Dubbo提供的Service注解，不是Spring提供的注解
public class UserServcieImpl implements UserService {
    @Override
    public String sayHello() {
        return "Hello Dubbo!";
    }
}
