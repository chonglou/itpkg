package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by flamen on 15-7-21.
 */

@Entity
@Table(name = "locales", indexes = {
        @Index(columnList = "code"),
        @Index(columnList = "lang"),
        @Index(columnList = "code,lang", unique = true)
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Locale extends IdEntity {
    @Column(nullable = false)
    private String code;
    @Column(nullable = false, length = 5)
    private String lang;
    @Lob
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
