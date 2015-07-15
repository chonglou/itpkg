package com.itpkg.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "roles")
public class Role extends IdEntity {
    @Column(nullable = false)
    @ManyToOne
    private User user;
    @Column(nullable = false)
    private String name;
    private String rType;
    private Integer rId;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getrType() {
        return rType;
    }

    public void setrType(String rType) {
        this.rType = rType;
    }

    public Integer getrId() {
        return rId;
    }

    public void setrId(Integer rId) {
        this.rId = rId;
    }
}
