package com.itpkg.core.auth;

import com.itpkg.core.services.SettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * Created by flamen on 15-7-27.
 */
@Component("core.tokenService")
public class TokenService {
    private final static Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String generate() {
        return UUID.randomUUID().toString();
    }

    public void store(String token, Authentication authentication) {
        redis.opsForValue().set(token2id(token), authentication, expire * 1000);
    }

    public boolean contains(String token) {
        return redis.hasKey(token2id(token));
    }

    public Authentication retrieve(String token) {
        return redis.opsForValue().get(token2id(token));
    }

    private String token2id(String token) {
        return "token://" + token;
    }

    @PostConstruct
    void init() {
        String kk = "site.token.expire";
        Long val = settingService.get(kk, Long.class);
        if (val == null) {
            expire = 30 * 60;
        } else {
            expire = val;
        }
    }

    //seconds
    private long expire;

    @Autowired
    RedisTemplate<String, Authentication> redis;
    @Autowired
    SettingService settingService;
}
