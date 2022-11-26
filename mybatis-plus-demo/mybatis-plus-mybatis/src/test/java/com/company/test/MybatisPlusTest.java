package com.company.test;

import com.baomidou.mybatisplus.core.MybatisSqlSessionFactoryBuilder;
import com.company.mapper.UserMapper;
import com.company.pojo.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @ author： Real
 * @ date： 2021年06月25日 14:41
 */
public class MybatisPlusTest {

    @Test
    public void testFindAll() throws IOException {
        // 1. 创建 sqlSessionFactory
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sessionFactory = new MybatisSqlSessionFactoryBuilder().build(resourceAsStream);

        // 2. 创建mapper对象
        SqlSession sqlSession = sessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);

        // 3. 运行测试
        // List<User> users = mapper.findAll();
        List<User> users = mapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
    }
}
