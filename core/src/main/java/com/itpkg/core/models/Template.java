package com.itpkg.core.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "templates")
public class Template implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private Date updated;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Lob
    private String body;
    @Column(nullable = false, length = 5)
    private String lang;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }


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
