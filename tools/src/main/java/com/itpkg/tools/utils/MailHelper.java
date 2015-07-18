package com.itpkg.tools.utils;

import com.itpkg.tools.models.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by flamen on 15-7-17.
 */
@Component
public class MailHelper extends DatabaseHelper {
    @PostConstruct
    public void init() throws ClassNotFoundException {
        jdbcTemplate = new JdbcTemplate();
        Configuration cfg = configHelper.read(Configuration.class);
        if (cfg != null) {
            super.init(cfg.getMail());
        }
    }

    @Autowired
    ConfigHelper configHelper;

}
