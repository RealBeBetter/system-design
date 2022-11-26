package com.example.mybatisplus_springboot;

import com.example.mybatisplus_springboot.mapper.UserMapper;
import com.example.mybatisplus_springboot.pojo.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class MybatisPlusSpringBootApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectList() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }

}
