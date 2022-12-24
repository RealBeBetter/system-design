package com.company.distributedid.service.impl;

import com.company.distributedid.service.RedisIdService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Real
 * Date: 2022/10/16 19:50
 */
@Service
public class RedisIdServiceImpl implements RedisIdService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public synchronized Long getMaxId() {
        String key = "id";
        redisTemplate.opsForValue().setIfAbsent(key, 0);
        redisTemplate.opsForValue().increment(key);
        Object max = redisTemplate.opsForValue().get(key);
        String maxStr = String.valueOf(max);
        return Long.valueOf(maxStr);
    }
}
