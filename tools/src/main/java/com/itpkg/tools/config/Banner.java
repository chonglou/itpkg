package com.itpkg.tools.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultBannerProvider;
import org.springframework.shell.support.util.OsUtils;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Banner extends DefaultBannerProvider {

    public String getBanner() {

        String s = "=======================================" + OsUtils.LINE_SEPARATOR;
        s += "*                                     *" + OsUtils.LINE_SEPARATOR;
        s += "*            " + Constants.APP_TITLE + "               *" + OsUtils.LINE_SEPARATOR;
        s += "*                                     *" + OsUtils.LINE_SEPARATOR;
        s += "=======================================" + OsUtils.LINE_SEPARATOR;
        s += "Version: " + this.getVersion();
        return s;
    }

    @Override
    public String getVersion() {
        return "v20150617";
    }

    @Override
    public String getWelcomeMessage() {
        return "Welcome to " + Constants.APP_TITLE + " CLI";
    }

    @Override
    public String getProviderName() {
        return Constants.APP_TITLE + " Banner";
    }

}
