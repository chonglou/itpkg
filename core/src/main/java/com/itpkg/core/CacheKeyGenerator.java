package com.itpkg.core;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * Created by flamen on 15-8-4.
 */
public class CacheKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        for (Object p : params) {
            sb.append("/");
            sb.append(p.toString());
        }
        return sb.toString();
    }
}
