package com.itpkg.core.models;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-16.
 */
@MappedSuperclass
public class IdEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private Date updated;


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
}