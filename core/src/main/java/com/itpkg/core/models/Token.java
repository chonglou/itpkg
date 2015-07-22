package com.itpkg.core.models;

/**
 * Created by flamen on 15-7-22.
 */
public class Token {
    public enum Action {
        CONFIRM, SIGN_IN, UNLOCK, CHANGE_PASSWORD
    }

    private long id;
    private Action action;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}
