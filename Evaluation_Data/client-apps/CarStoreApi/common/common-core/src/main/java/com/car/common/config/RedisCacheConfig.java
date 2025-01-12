package com.car.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisTemplate<Object,Object> billredisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Object> templateFor = new RedisTemplate<Object,Object>();
        templateFor.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Object> ser = new Jackson2JsonRedisSerializer<Object>(Object.class);
        templateFor.setDefaultSerializer(ser);
        return templateFor;
    }
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        return redisTemplate;
    }
}
