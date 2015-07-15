package com.itpkg.core.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
public class Log implements Serializable {
    private int id;
    private String message;
    private Date created;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
