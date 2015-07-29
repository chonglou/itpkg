package com.itpkg.core.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by flamen on 15-7-22.
 */
public class UserToken implements Serializable {
    public UserToken() {
        params = new HashMap<>();
    }

    public UserToken(long uid, String action) {
        this();
        this.uid = uid;
        this.action = action;
        this.tid = UUID.randomUUID().toString();
    }

    private long uid;
    private String tid;
    private String action;
    private Map<String, Object> params;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
