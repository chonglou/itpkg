package com.itpkg.core.controllers;

import com.itpkg.core.auth.CurrentUser;
import com.itpkg.core.auth.Rule;
import com.itpkg.core.models.User;
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
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flamen on 15-7-14.
 */

@Controller("core.controllers.site")
@RequestMapping("/site")
public class SiteController {
    private static final Logger logger = LoggerFactory.getLogger(SiteController.class);

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Map<String, String> info() {
        Map<String, String> map = new HashMap<>();
        for (String s : new String[]{"title", "description", "keywords", "copyright"}) {
            map.put(s, i18n.T("site." + s));
        }
        return map;
    }

    public class UrlInfo {
        public String method;
        public String url;
        public String controller;
    }

    @Rule(role = "admin")
    @RequestMapping(value = "/routes", method = RequestMethod.GET)
    @ResponseBody
    public List<UrlInfo> routes(@CurrentUser User currentUser) {
        List<UrlInfo> urls = new ArrayList<>();
        requestMappingHandlerMapping.getHandlerMethods().forEach((k, v) -> {
            for (RequestMethod m : k.getMethodsCondition().getMethods()) {
                for (String u : k.getPatternsCondition().getPatterns()) {
                    UrlInfo ui = new UrlInfo();
                    ui.method = m.toString();
                    ui.url = u;
                    ui.controller = v.getMethod().toString();
                    urls.add(ui);
                }
            }
        });

        return urls;

    }


    @Autowired
    UserService userService;

    @Autowired
    I18nService i18n;

    @Autowired
    SettingService settingService;

    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;

}
