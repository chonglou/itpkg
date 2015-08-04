package com.itpkg.core;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by flamen on 15-8-4.
 */
public class RedisCachePrefix implements org.springframework.data.redis.cache.RedisCachePrefix {
    public RedisCachePrefix() {
        serializer = new StringRedisSerializer();
    }

    @Override
    public byte[] prefix(String cacheName) {
        return serializer.serialize("cache://" + cacheName + "/");
    }

    private final RedisSerializer<String> serializer;
}
