package com.itpkg.core.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-8-3.
 */
public class Smtp implements Serializable {
    public Smtp() {
        bcc = new ArrayList<>();
    }

    private String host;
    private int port;
    private boolean ssl;
    private String username;
    private String password;
    private List<String> bcc;
    private String from;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
