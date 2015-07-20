package com.itpkg.core.dao;

import com.itpkg.core.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by flamen on 15-7-15.
 */
@Repository("core.dao.template")
public interface TemplateDao extends JpaRepository<Template, Long> {
    Optional<Template> findByNameAndLang(String name, String lang);
}
