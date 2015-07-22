package com.itpkg.core.dao;

import com.itpkg.core.models.Locale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-21.
 */
@Repository("core.dao.locale")
public interface LocaleDao extends JpaRepository<Locale, Long> {
    Locale findByCodeAndLang(String code, String lang);
}
