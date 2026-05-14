package com.smartlease.smartlease_backend.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlackListService {

    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public TokenBlackListService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void blacklistToken(String token, long expirationInMs){
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + token, //key
                "blacklisted", //value
                expirationInMs, //TTL duration
                TimeUnit.MILLISECONDS //TTL unit
        );
    }

    public boolean isBlacklisted(String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }
}
