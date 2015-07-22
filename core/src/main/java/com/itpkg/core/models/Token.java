package com.itpkg.core.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 15-7-22.
 */
public class Token {
    public Token() {
        params = new HashMap<>();
    }

    public Token(long uid, String action) {
        this();
        this.uid = uid;
        this.action = action;
    }

    private long uid;
    private String action;
    private Map<String, Object> params;

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
