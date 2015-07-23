package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-23.
 */

@Entity
@Table(name = "sessions")
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Session implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, updatable = false, unique = true, length = 800)
    private String payload;
    @Column(nullable = false, updatable = false)
    private Date created;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

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
}
