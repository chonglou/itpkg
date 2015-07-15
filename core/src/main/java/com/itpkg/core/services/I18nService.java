package com.itpkg.core.services;

import com.itpkg.core.dao.TemplateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by flamen on 15-7-15.
 */
@Service
public class I18nService {
    public String t(String name, String lang, Object... args) {
        return templateDao.findByNameAndLang(name, lang)
                .map((t) -> String.format(t.getBody(), args))
                .orElse(String.format("Translation %s.%s not exists!", name, lang));
    }

    @Autowired
    private TemplateDao templateDao;

    public void setTemplateDao(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }
}
