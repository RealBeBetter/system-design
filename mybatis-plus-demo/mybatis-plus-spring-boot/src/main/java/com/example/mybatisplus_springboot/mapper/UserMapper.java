package com.example.mybatisplus_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.mybatisplus_springboot.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ author： Real
 * @ date： 2021年06月30日 16:31
 */
@Repository
public interface UserMapper extends MyBaseMapper<User> {
    User findById(Long id);

    List<User> findAll(Long id);
}
