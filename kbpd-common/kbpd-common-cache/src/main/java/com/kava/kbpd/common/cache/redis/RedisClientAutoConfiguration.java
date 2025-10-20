package com.kava.kbpd.common.cache.redis;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author Kris
 * @date 2025/3/18
 * @description: Redis 客户端，使用 Redisson
 */
@Data
@Configuration
@EnableConfigurationProperties(RedisClientProperties.class)
public class RedisClientAutoConfiguration {

    @Bean
    public RedisKeyGenerator redisKeyGenerator(@Value("${spring.profiles.active:local}") String active) {
        return new RedisKeyGenerator(active);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisClientProperties properties) {
        // 配置Lettuce连接池
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofMillis(properties.getIdleTimeout()))
                .build();

        // 配置Redis地址和密码
        RedisStandaloneConfiguration serverConfig = new RedisStandaloneConfiguration(
                properties.getHost(),
                properties.getPort()
        );
        serverConfig.setPassword(RedisPassword.of(properties.getPassword()));

        return new LettuceConnectionFactory(serverConfig, clientConfig);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.string());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedissonClient redissonClient(RedisClientProperties properties) {
        Config config = new Config();
        // 根据需要可以设定编解码器；https://github.com/redisson/redisson/wiki/4.-%E6%95%B0%E6%8D%AE%E5%BA%8F%E5%88%97%E5%8C%96
        config.setCodec(new StringCodec());
        config.setThreads(5);
        config.setNettyThreads(5);

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
//                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive())
        ;

        return Redisson.create(config);
    }

    @Bean
    public RedisLock redisLock(RedissonClient redissonClient) {
        return new RedisLock(redissonClient);
    }

}

