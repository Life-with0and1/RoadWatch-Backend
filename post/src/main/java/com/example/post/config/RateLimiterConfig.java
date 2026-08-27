package com.example.post.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public RedisClient redisClient() {

        String host = System.getenv("REDISHOST");
        String port = System.getenv("REDISPORT");
        String password = System.getenv("REDISPASSWORD");

        String redisUrl = String.format(
                "redis://:%s@%s:%s",
                password,
                host,
                port
        );

        return RedisClient.create(redisUrl);
    }

    @Bean
    public StatefulRedisConnection<String, byte[]> redisConnection(RedisClient redisClient) {
        RedisCodec<String, byte[]> codec = RedisCodec.of(StringCodec.UTF8,ByteArrayCodec.INSTANCE);
        return redisClient.connect(codec);
    }

    @Bean
    public ProxyManager<String> proxyManager(StatefulRedisConnection<String, byte[]> redisConnection) {
        return io.github.bucket4j.redis.lettuce.Bucket4jLettuce
                .casBasedBuilder(redisConnection)
                .build();
    }
    
}