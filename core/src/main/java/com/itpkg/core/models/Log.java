package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "logs")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Log implements Serializable {
    public enum Type {
        INFO
    }

    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @JoinColumn
    private User user;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private Type type;
    @Column(nullable = false)
    private Date created;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
