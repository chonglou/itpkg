package com.itpkg.core.services;

import com.itpkg.core.dao.LocaleDao;
import com.itpkg.core.models.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by flamen on 15-7-21.
 */

@Service("core.service.locale")
public class LocaleService {

    @CacheEvict(cacheNames = "locales", key = "#locale.toString()+'/'+ #code")
    public void set(String code, java.util.Locale locale, String message) {
        String lang = locale.toString();
        Locale l = localeDao.findByCodeAndLang(code, lang);
        Date now = new Date();
        if (l == null) {
            l = new Locale();
            l.setCode(code);
            l.setLang(lang);
            l.setCreated(now);
        }
        l.setMessage(message);
        l.setUpdated(now);
        localeDao.save(l);
    }

    @Cacheable(cacheNames = "locales", key = "#locale.toString()+'/'+ #code")
    public String get(String code, java.util.Locale locale) {
        String lang = locale.toString();
        Locale l = localeDao.findByCodeAndLang(code, lang);
        return l == null ? null : l.getMessage();
    }

    @Autowired
    LocaleDao localeDao;
}
