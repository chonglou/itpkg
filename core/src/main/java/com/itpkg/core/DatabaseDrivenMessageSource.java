package com.itpkg.core;

import com.itpkg.core.services.LocaleService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractMessageSource;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Created by flamen on 15-7-21.
 */

public class DatabaseDrivenMessageSource extends AbstractMessageSource {
    private final static Logger logger = LoggerFactory.getLogger(DatabaseDrivenMessageSource.class);

    public void init() {
        logger.info("load locales from: " + StringUtils.join(basenames, ", "));
        for (String basename : basenames) {
            for (Locale locale : locales) {
                ResourceBundle rb = ResourceBundle.getBundle(basename + "_" + locale);
                Set<String> keys = rb.keySet();
                keys.forEach((key) -> {
                    //todo 会有性能问题
                    if (localeService.get(key, locale) == null) {
                        localeService.set(key, locale, rb.getString(key));
                    }
                });
            }
        }
    }


    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String msg = localeService.get(code, locale);
        return createMessageFormat(msg, locale);
    }

    private LocaleService localeService;
    private String[] basenames;
    private Locale[] locales;

    public void setLocaleService(LocaleService localeService) {
        this.localeService = localeService;
    }

    public void setBasenames(String[] basenames) {
        this.basenames = basenames;
    }

    public void setLocales(Locale[] locales) {
        this.locales = locales;
    }
}
