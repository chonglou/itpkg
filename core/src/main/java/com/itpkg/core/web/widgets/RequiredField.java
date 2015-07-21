package com.itpkg.core.web.widgets;

/**
 * Created by flamen on 15-7-20.
 */
public class RequiredField extends Field {
    public RequiredField(String id, String type, String name) {
        super(id, type);
        this.name = name;
    }
    private String name;
    private boolean required;
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
