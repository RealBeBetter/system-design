package com.example.mybatisplus_springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @ author： Real
 * @ date： 2021年07月02日 10:45
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {

    List<T> findAll();
}
