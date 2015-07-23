package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-23.
 */

@Entity
@Table(name = "sessions")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Session extends IdEntity {
    @Column(nullable = false, updatable = false, unique = true, length = 800)
    private String payload;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
