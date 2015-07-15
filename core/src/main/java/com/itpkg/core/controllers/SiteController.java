package com.itpkg.core.controllers;

import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by flamen on 15-7-14.
 */

@RestController
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping("/site/{name:.+}")
    String info(@PathVariable String name) {
        switch (name) {
            case "title":
            case "copyright":
                return i18n.t("site."+name, "en-US");
            default:
                return name;
        }
    }

    @Autowired
    private UserService userService;

    @Autowired
    private I18nService i18n;

    @Autowired
    private SettingService settingService;

    public void setI18n(I18nService i18n) {
        this.i18n = i18n;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }
}
