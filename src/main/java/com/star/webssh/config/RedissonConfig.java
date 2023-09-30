package com.star.webssh.config;

import io.swagger.models.auth.In;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sound.sampled.Port;

/**
 *@ClassName RedissonConfig
 *@Description 限流操作的配置类,来连接redis
 *@Author Mr.Wang
 *@Date 2023/9/30 17:46
 *@Version 1.0
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedissonConfig {

    private String host;

    private Integer database;

    private Integer port;

    private String password;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();

        config.useSingleServer()
                .setDatabase(database)
                .setPassword(password)
                .setAddress("redis://"+host+":"+port);
        RedissonClient redissonClient = Redisson.create(config);

        return redissonClient;

    }

}


