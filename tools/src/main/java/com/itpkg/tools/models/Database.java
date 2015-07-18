package com.itpkg.tools.models;

import java.io.Serializable;

/**
 * Created by flamen on 15-7-17.
 */
public class Database implements Serializable {
    public static Database mysql() {
        Database db = new Database();
        db.type = "mysql";
        db.host = "localhost";
        db.port = 3306;
        db.user = "root";
        db.name = "mail";
        return db;
    }

    public String getUrl() {
        return String.format("jdbc:%s://%s:%d/%s", type, host, port, name);
    }

    public String getDriver() {
        switch (type) {
            case "mysql":
                return "com.mysql.jdbc.Driver";
            case "postgresql":
                return "org.postgresql.Driver";
            default:
                throw new IllegalArgumentException();
        }
    }

    private String type;
    private String host;
    private int port;
    private String user;
    private String name;
    private String password;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
