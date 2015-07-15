package com.itpkg.core.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "settings")
public class Setting implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private Date updated;
    @Column(nullable = false, unique = true, name = "key_")
    private String key;

    @Column(nullable = false)
    @Lob
    private String val;

    private boolean encode;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public boolean isEncode() {
        return encode;
    }

    public void setEncode(boolean encode) {
        this.encode = encode;
    }
}
