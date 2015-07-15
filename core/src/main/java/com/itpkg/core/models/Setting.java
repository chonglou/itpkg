package com.itpkg.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "settings")
public class Setting extends IdEntity {
    @Column(nullable = false, unique = true)
    private String key;

    @Column(nullable = false)
    @Lob
    private String val;

    @Column(length = 16)
    private byte[] iv;


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

    public byte[] getIv() {
        return iv;
    }

    public void setIv(byte[] iv) {
        this.iv = iv;
    }

}
