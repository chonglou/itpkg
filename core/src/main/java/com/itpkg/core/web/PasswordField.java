package com.itpkg.core.web;

/**
 * Created by flamen on 15-7-17.
 */
public class PasswordField extends Field {
    public PasswordField(String id, String name) {
        super(id, "password");
        this.name = name;
    }

    private int size;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
