package com.itpkg.core.utils;

import com.itpkg.core.models.Smtp;
import com.itpkg.core.services.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by flamen on 15-8-3.
 */
@Component("core.siteHelper")
public class SiteHelper {
    @PostConstruct
    void init(){
        if(settingService.get("site.smtp", Smtp.class) == null){
            Smtp smtp = new Smtp();
            smtp.setHost("localhost");
            smtp.setPort(25);
            smtp.setFrom("no-reply@localhost");
            settingService.set("site.smtp", smtp);
        }
        if(settingService.get("site.url", String.class) == null){
            settingService.set("site.url", "http://localhost:8088");
        }

    }
    @Autowired
    SettingService settingService;
}
