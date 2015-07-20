package com.itpkg.core.controllers;

import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 15-7-14.
 */

@Controller("core.controllers.site")
public class SiteController {
    private final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping(value = "/site/info", method = RequestMethod.GET)
    @ResponseBody
    Map<String, String> info() {
        Map<String, String> map = new HashMap<>();
        for (String s : new String[]{"title", "description", "keywords", "copyright"}) {
            map.put(s, i18n.T("site." + s));
        }
        return map;
    }

    @Autowired
    UserService userService;

    @Autowired
    I18nService i18n;

    @Autowired
    SettingService settingService;


}
