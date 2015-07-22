package com.itpkg.core;

import com.itpkg.core.services.LocaleService;
import org.parancoe.web.util.ReloadableResourceBundleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Created by flamen on 15-7-21.
 */

public class DatabaseDrivenMessageSource extends AbstractMessageSource {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseDrivenMessageSource.class);


    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = localeService.get(code, locale);
        if (msg == null) {
            MessageSource ms = getParentMessageSource();
            if (ms instanceof ReloadableResourceBundleMessageSource) {
                return ((ReloadableResourceBundleMessageSource) ms).resolveCode(code, locale);
            }
        }
        return createMessageFormat(msg, locale);
    }

    private LocaleService localeService;

    public void setLocaleService(LocaleService localeService) {
        this.localeService = localeService;
    }
}
