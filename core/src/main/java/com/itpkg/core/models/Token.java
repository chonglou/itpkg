package com.itpkg.core.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "tokens")
public class Token extends IdEntity {

    @Column(nullable = false)
    private String body;

    @Column(length = 16)
    private byte[] key;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] key) {
        this.key = key;
    }
}
