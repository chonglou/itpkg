package com.itpkg.cms.models;

import com.itpkg.core.models.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-16.
 */

@Entity
@Table(name = "cms_tags")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tag extends IdEntity {
    @Column(nullable = false, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
