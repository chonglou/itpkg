package com.itpkg.core.dao;

import com.itpkg.core.models.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by flamen on 15-7-15.
 */
public interface TemplateDao extends JpaRepository<Template, Long> {
    Optional<Template> findByNameAndLang(String name, String lang);
}
