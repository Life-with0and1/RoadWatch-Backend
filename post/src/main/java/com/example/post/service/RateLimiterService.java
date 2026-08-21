package com.example.post.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimiterService {

    private final ProxyManager<String> proxyManager;

    public RateLimiterService(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public boolean tryConsume(Long userId) {
        String key = "post-create:" + userId;
        BucketConfiguration configuration = BucketConfiguration.builder().addLimit(limit -> limit.capacity(5).refillGreedy(1, Duration.ofMinutes(2)))
                .build();

        Bucket bucket = proxyManager.getProxy(key,() -> configuration);

        return bucket.tryConsume(1);
    }


    public boolean tryConsumeVote(Long userId) {
        String key = "vote:" + userId;
        
        BucketConfiguration configuration = BucketConfiguration.builder()
                        .addLimit(limit -> limit
                        .capacity(10)
                        .refillGreedy(1, Duration.ofSeconds(10))
                )
                .build();

        Bucket bucket = proxyManager.getProxy(key,() -> configuration);

        return bucket.tryConsume(1);
    }
}