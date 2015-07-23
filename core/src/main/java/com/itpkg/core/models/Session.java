package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by flamen on 15-7-23.
 */

@Entity
@Table(name = "sessions", indexes = {
        @Index(columnList = "jid"),
        @Index(columnList = "jid,user_id", unique = true)
})
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Session extends IdEntity {
    @Column(nullable = false, updatable = false, length = 16)
    private String jid;
    @Column(name = "begin_", nullable = false, updatable = false)
    private Date begin;
    @Column(name = "end_", nullable = false, updatable = false)
    private Date end;
    @Column(nullable = false, updatable = false)
    @Lob
    private String payload;
    @ManyToOne
    @JoinColumn(updatable = false)
    private User user;
    private boolean enable;

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
