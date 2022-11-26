package com.example.mybatisplus_springboot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mybatisplus_springboot.enums.SexEnum;
import com.example.mybatisplus_springboot.mapper.UserMapper;
import com.example.mybatisplus_springboot.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @ author： Real
 * @ date： 2021年07月01日 19:55
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ActiveRecordTest {

    /**
     * 在别处已经注入ioc容器，可以省略，实际上也不需要使用
     */
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testAR() {
        User user = new User();
        user.setId(2L);

        User user1 = user.selectById();
        System.out.println(user1);
    }

    /**
     * 测试自动填充字段
     */
    @Test
    public void testInsert() {
        User user = new User();
        user.setUserName("qianqi");
        // user.setPassword("123456");
        user.setName("钱七");
        user.setAge(25);
        user.setEmail("test5@itcast.cn");

        boolean insert = user.insert();
        System.out.println(insert);
        User user1 = user.selectById();
        System.out.println(user1);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setId(3L);
        user.setAge(26);

        boolean update = user.updateById();
        System.out.println(update);
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setId(4L);

        boolean delete = user.deleteById();
        System.out.println(delete);
    }

    @Test
    public void testSelect() {
        User user = new User();

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ge("age", 22);  // 查询年龄>=22岁的用户

        List<User> users = user.selectList(wrapper);
        for (User user1 : users) {
            System.out.println(user1);
        }
    }

    /**
     * 全表更新测试，测试阻断器是否生效
     * 报错，表示生效，执行阻断
     */
    @Test
    public void testUpdateAll() {
        User user = new User();
        user.setAge(20);

        boolean update = user.update(null);
        System.out.println(update);
    }

    /**
     * 乐观锁测试
     */
    @Test
    public void testUpdateVersion() {
        User user = new User();
        user.setId(1L);
        user.setAge(26);
        user.setVersion(1);

        boolean update = user.updateById();
        System.out.println(update);
    }

    /**
     * 自定义 SQL 注入器方法测试
     */
    @Test
    public void testFindAll(){
        List<User> users = userMapper.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    /**
     * 逻辑删除测试
     */
    @Test
    public void testDeleteById(){
        this.userMapper.deleteById(4L);
    }

    /**
     * 逻辑删除测试查询结果
     */
    @Test
    public void testSelectById(){
        User user = this.userMapper.selectById(4L);
        System.out.println(user);
    }

    /**
     * 通用枚举测试
     */
    @Test
    public void testInsertEnum(){
        User user = new User();
        user.setName("钱七");
        user.setUserName("qianqi");
        user.setAge(20);
        user.setEmail("test5@itast.cn");
        user.setVersion(1);
        user.setSex(SexEnum.WOMAN);
        int result = this.userMapper.insert(user);
        System.out.println("result = " + result);
    }

    /**
     * 测试枚举查询
     */
    @Test
    public void testSelectBySex() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("sex", SexEnum.WOMAN);
        List<User> users = this.userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }


}
