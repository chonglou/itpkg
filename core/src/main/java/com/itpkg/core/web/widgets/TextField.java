package com.itpkg.core.web.widgets;

/**
 * Created by flamen on 15-7-17.
 */
public class TextField extends Field {
    public TextField(String id, String name) {
        super(id, "text");
        this.name = name;
    }

    private String value;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
