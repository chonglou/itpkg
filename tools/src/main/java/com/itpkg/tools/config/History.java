package com.itpkg.tools.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultHistoryFileNameProvider;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class History extends DefaultHistoryFileNameProvider {
    @Override
    public String getHistoryFileName() {
        return "commands.log";
    }

    @Override
    public String getProviderName() {
        return Constants.APP_TITLE + " history file name provider";
    }

}
