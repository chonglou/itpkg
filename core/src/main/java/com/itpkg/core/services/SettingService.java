package com.itpkg.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by flamen on 15-7-14.
 */
@Service
public class SettingService {
    private final Logger logger = LoggerFactory.getLogger(SettingService.class);

    public void set(String key, String val) {
        set(key, val, false);
    }

    public void set(String key, String val, boolean encode) {
        // todo

    }

    public void get(String key, String val) {
        // todo
    }

}
