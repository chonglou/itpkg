package com.itpkg.tools.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.shell.plugin.support.DefaultPromptProvider;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-17.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class Prompt extends DefaultPromptProvider {
    @Override
    public String getPrompt() {
        return Constants.APP_NAME + "> ";
    }

    @Override
    public String getProviderName() {
        return Constants.APP_TITLE;
    }
}
