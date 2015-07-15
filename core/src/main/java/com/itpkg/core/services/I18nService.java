package com.itpkg.core.services;

import com.itpkg.core.dao.TemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by flamen on 15-7-15.
 */
@Service
public class I18nService {

    public String T(String name, Object... args) {
        return t(name, LocaleContextHolder.getLocale(), args);
    }

    public String t(String name, Locale locale, Object... args) {
        return templateDao.findByNameAndLang(name, locale.getDisplayName())
                .map((t) -> String.format(t.getBody(), args))
                .orElse(messageSource.getMessage(name, args, locale));
        //String.format("Translation %s.%s not exists!", name, lang)
    }

    @Autowired
    TemplateDao templateDao;
    @Autowired
    MessageSource messageSource;

}
