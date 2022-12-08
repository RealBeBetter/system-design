package com.company.antibrushing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Real
 * @since 2022/12/9 0:59
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用 Jackson2JsonRedisSerializer 替换默认的 JdkSerializationRedisSerializer
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        // 设置 key 的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        // 设置 value 的序列化方式
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // 设置 hash 的 key 的序列化方式
        template.setHashKeySerializer(new StringRedisSerializer());
        // 设置 hash 的 value 的序列化方式
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }
}

