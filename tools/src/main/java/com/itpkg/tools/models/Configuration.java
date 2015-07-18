package com.itpkg.tools.models;

import java.io.Serializable;

/**
 * Created by flamen on 15-7-17.
 */
public class Configuration implements Serializable {
    private Database mail;

    public Database getMail() {
        return mail;
    }

    public void setMail(Database mail) {
        this.mail = mail;
    }
}
