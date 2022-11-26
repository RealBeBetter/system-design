package com.example.mybatisplus_springboot;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mybatisplus_springboot.mapper.UserMapper;
import com.example.mybatisplus_springboot.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ author： Real
 * @ date： 2021年06月30日 17:23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CRUDTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testInsert() {
        User user = new User();
        user.setEmail("swrely@qq.com");
        user.setUserName("Real");
        user.setName("雨下一整晚");
        user.setAge(20);
        user.setPassword("123456");
        // 返回的是数据库影响的行数
        int insert = this.userMapper.insert(user);
        System.out.println("Result >= " + insert);

        // 获取自增长后的id，插入成功后自增站的id值会回填到user对象中
        System.out.println("id >= " + user.getId());
    }

    @Test
    public void testSelectById() {
        User user = userMapper.selectById(2);
        System.out.println(user);
    }

    @Test
    public void testUpdateById() {
        User user = new User();
        user.setId(6L);
        user.setName("钱八");
        user.setUserName("qianba");
        user.setEmail("test6@itcast.cn");
        int update = userMapper.updateById(user);   // 返回的是数据库影响的行数
        System.out.println(update);
        User selectUser = userMapper.selectById(6L);
        System.out.println(selectUser);
    }

    @Test
    public void testUpdate() {
        User user = new User();
        user.setAge(20);
        user.setPassword("888888");
        // Wrapper 包装器对象的编写
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "zhangsan");
        int update = userMapper.update(user, queryWrapper);
        System.out.println(update);
        System.out.println(userMapper.selectById(1L));
    }

    @Test
    public void testDirectUpdate() {
        // Wrapper 包装器对象
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("password", "999999").eq("user_name", "zhangsan");
        int update = userMapper.update(null, wrapper);
        System.out.println(update);
        System.out.println(userMapper.selectById(1L));
    }

    @Test
    public void testDeleteById() {
        int delete = userMapper.deleteById(6L);
        System.out.println(delete);
        System.out.println(userMapper.selectList(null));
    }

    @Test
    public void testDeleteByMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("user_name", "zhangsan");
        map.put("password", "123456");      // password is “999999”
        // the relationship of maps is AND in sql statement
        System.out.println(userMapper.deleteByMap(map));    // 0
        System.out.println(userMapper.selectById(1L));      // same as before
    }

    @Test
    public void testDelete() {
        // 方法一：创建 Wrapper对象
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", "5");
        System.out.println(userMapper.delete(wrapper));     // 1
        System.out.println(userMapper.selectById(5L));      // null

        // 方法二：创建user对象
        User user = new User();
        user.setId(5L);
        user.setPassword("123456");
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        System.out.println(userMapper.delete(queryWrapper));    // 0
        System.out.println(userMapper.selectById(5L));          // null
    }

    @Test
    public void testDeleteBatchIds() {
        // 根据id进行批量删除
        int delete = userMapper.deleteBatchIds(Arrays.asList(7L, 8L));  // 2
        System.out.println(delete);
    }

    @Test
    public void testSelectBatchIds() {
        // 根据id批量查询
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1L, 2L));
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectOne() {
        // 根据条件查询单个
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", "wangwu");
        // 查询到的数据超过一条时，会抛出异常
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
    }

    @Test
    public void testSelectCount() {
        // 根据条件查询，返回数据条数
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.gt("age", "20");    // age > 20 , count = 2
        Integer count = userMapper.selectCount(wrapper);
        System.out.println(count);      // 2
    }

    @Test
    public void testSelectList() {
        // 根据条件查询全部记录
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("email", "@");             // like %@%
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelectPage() {
        // 根据条件进行分页查询
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like("email", "@");

        Page<User> page = new Page<>(1, 1);
        IPage<User> iPage = userMapper.selectPage(page, wrapper);
        System.out.println("总条数：" + iPage.getTotal());
        System.out.println("总页数：" + iPage.getPages());
        System.out.println("当前页：" + iPage.getCurrent());

        List<User> records = iPage.getRecords();
        for (User record : records) {
            System.out.println(record);
        }

    }

    @Test
    public void testFindById() {
        User user = userMapper.findById(1L);
        System.out.println(user);
    }

    @Test
    public void testAllEq() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        // 设置allEq的参数条件
        Map<String, Object> map = new HashMap<>();
        map.put("name", "张三");
        map.put("age", "20");
        map.put("password", null);

        // 单个参数，多个map值之间的关系是AND，查询结果为空
        // wrapper.allEq(map);
        // 两个参数，设置为null的参数是否指定为查询条件，查询有结果
        // wrapper.allEq(map, false);
        // Lambda表达式参数，表示满足条件则添加进where条件中，有多个条件满足则使用AND连接
        wrapper.allEq((K, V) -> ("name".equals(K) || "age".equals(K) || "id".equals(K)), map);

        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testLike() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.likeLeft("user_name", "wu");
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testOrderByAgeDesc() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("age");
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testOr() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", "wangwu").or().eq("age", 20);
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testSelect() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", "wangwu").or()
                .eq("age", 20)
                .select("user_name", "name", "age");        // 指定查询的字段
        List<User> users = userMapper.selectList(wrapper);
        for (User user : users) {
            System.out.println(user);
        }
    }

}
