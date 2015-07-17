package com.itpkg.core.web;

/**
 * Created by flamen on 15-7-17.
 */
public class EmailField extends TextField {
    public EmailField(String id, String name) {
        super(id, name);
        setType("email");
    }
}
