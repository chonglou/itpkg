package com.itpkg.core.controllers;

import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by flamen on 15-7-14.
 */

@Controller
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping("/site/{name:.+}")
    String info(@PathVariable String name) {
        switch (name) {
            case "copyright":
            default:
                return name;
        }
    }

    @Resource
    private UserService userService;

    @Resource
    private SettingService settingService;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setSettingService(SettingService settingService) {
        this.settingService = settingService;
    }
}
