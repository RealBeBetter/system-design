package com.company.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.pojo.User;

import java.util.List;

/**
 * @author Real
 * @date 2021年06月30日 16:02
 */
public interface UserMapper extends BaseMapper<User> {
    /**
     * 查找所有User信息
     *
     * @return {@link List}<{@link User}>
     */
    List<User> findAll();
}
