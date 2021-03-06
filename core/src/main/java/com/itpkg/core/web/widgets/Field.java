package com.itpkg.core.web.widgets;

import java.io.Serializable;

/**
 * Created by flamen on 15-7-17.
 */
public abstract class Field<T> implements Serializable {
    public Field(String id, String type) {
        this.id = id;
        this.type = type;
    }

    private String id;
    private String type;
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
