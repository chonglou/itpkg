package com.itpkg.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "templates")
public class Template extends IdEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Lob
    private String body;
    @Column(nullable = false, length = 5)
    private String lang;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

}
