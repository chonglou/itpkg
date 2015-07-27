package com.itpkg.core.controllers;

import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.LocaleService;
import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import com.itpkg.core.utils.EngineHelper;
import com.itpkg.core.web.widgets.Link;
import com.itpkg.core.web.widgets.TopNavBar;
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

@Controller("core.controllers.home")
public class HomeController {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);


    @RequestMapping(value = "/nav_bar", method = RequestMethod.GET)
    @ResponseBody
    TopNavBar getNavBar() {
        //todo
        User currentUser = null;

        TopNavBar tnb = new TopNavBar();
        tnb.setTitle(i18n.T("site.title"));

        tnb.addHotLink(new Link("home", i18n.T("pages.home.title")));
        for (String en : engineHelper.getEngines()) {
            if (engineHelper.isEnable(en)) {
                tnb.addHotLink(new Link("engine." + en, i18n.T("engine." + en + ".name")));
            }
        }
        tnb.addHotLink(new Link("about_us", i18n.T("pages.about_us.title")));

        if (currentUser == null) {
            tnb.setBarName(i18n.T("user.sign_in_or_up"));
            for (String s : new String[]{"sign_in", "sign_up", "forgot_password", "confirm", "unlock"}) {
                tnb.addBarLink(new Link("users." + s, i18n.T("form.user." + s + ".title")));
            }
        } else {
            tnb.setBarName(i18n.T("user.personal_center"));
            tnb.addBarLink(new Link("users.profile", i18n.T("form.user.profile.title")));
        }
        return tnb;
    }


    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    Map<String, String> getCopyright() {
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

    @RequestMapping(value = "/routes", method = RequestMethod.GET)
    @ResponseBody
    public List<UrlInfo> routes() {
        //todo
        User currentUser = null;

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
    EngineHelper engineHelper;

    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    LocaleService localeService;

}
