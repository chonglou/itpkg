package com.itpkg.core.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private Date updated;
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
