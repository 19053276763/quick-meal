package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration // 标记这是配置类，项目启动时自动加载
@Slf4j
public class RedisConfiguration {

    @Bean // 把这个方法返回的RedisTemplate交给Spring容器管理，后面@Autowired直接注入用
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 1. 创建RedisTemplate对象
        RedisTemplate redisTemplate = new RedisTemplate();
        // 2. 设置连接工厂（里面包含redis地址、端口、密码，从yml配置读取）
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 3. 设置key的序列化器：StringRedisSerializer
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
