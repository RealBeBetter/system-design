package com.company.antibrushing.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Real
 * @since 2022/12/9 0:47
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置缓存
     *
     * @param key     缓存键
     * @param value   缓存值
     * @param timeout 缓存时间（单位：秒）
     */
    public void set(String key, Object value, long timeout) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取缓存值
     *
     * @param key 缓存键
     * @return 缓存值
     */
    public Object get(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

}
