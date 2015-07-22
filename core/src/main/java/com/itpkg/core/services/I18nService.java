package com.itpkg.core.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by flamen on 15-7-15.
 */
@Service("core.service.i18n")
public class I18nService {

    public String T(String code, Object... args) {
        return t(code, LocaleContextHolder.getLocale(), args);
    }

    public String t(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    @Autowired
    @Qualifier("databaseDrivenMessageSource")
    MessageSource messageSource;

}
