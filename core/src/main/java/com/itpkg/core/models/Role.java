package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "roles", indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "rType"),
        @Index(columnList = "name,rtype,rid,user_id", unique = true)
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {

    @ManyToOne
    @JoinColumn
    private User user;
    @Column(nullable = false)
    private String name;
    private String rType;
    private Integer rId;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startUp;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date shutDown;

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

    public Date getStartUp() {
        return startUp;
    }

    public void setStartUp(Date startUp) {
        this.startUp = startUp;
    }

    public Date getShutDown() {
        return shutDown;
    }

    public void setShutDown(Date shutDown) {
        this.shutDown = shutDown;
    }
}
