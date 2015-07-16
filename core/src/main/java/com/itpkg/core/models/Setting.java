package com.itpkg.core.models;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "settings")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Setting extends IdEntity {


    @Column(nullable = false, unique = true, name = "key_")
    private String key;

    @Column(nullable = false)
    @Lob
    private String val;

    private boolean encode;

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
