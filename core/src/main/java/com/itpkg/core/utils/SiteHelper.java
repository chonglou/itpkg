package com.itpkg.core.utils;

import com.itpkg.core.models.Smtp;
import com.itpkg.core.models.User;
import com.itpkg.core.services.RoleService;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by flamen on 15-8-3.
 */
@Component("core.siteHelper")
public class SiteHelper {
    @PostConstruct
    void init() {
        if (settingService.get("site.smtp", Smtp.class) == null) {
            Smtp smtp = new Smtp();
            smtp.setHost("localhost");
            smtp.setPort(25);
            smtp.setFrom("no-reply@localhost");
            settingService.set("site.smtp", smtp);
        }
        if (settingService.get("site.url", String.class) == null) {
            settingService.set("site.url", "http://localhost:8088");
        }

        if (userService.count() == 0) {
            User u = userService.create("root", "root@localhost", "changeme");
            userService.setConfirmed(u.getId());
            roleService.set(u.getId(), "admin");
            roleService.set(u.getId(), "root");
        }

        jobContainer.addMessageListener(emailJobListener, new PatternTopic("emails"));


    }

    @Autowired
    SettingService settingService;
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    RedisMessageListenerContainer jobContainer;
    @Autowired
    @Qualifier("core.emailJobListener")
    MessageListenerAdapter emailJobListener;
}
