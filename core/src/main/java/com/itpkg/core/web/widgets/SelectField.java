package com.itpkg.core.web.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 15-7-18.
 */
public class SelectField<T> extends Field {
    public SelectField(String id) {
        super(id, "select");
        options = new HashMap<>();
    }

    public void addOption(T key, String name) {
        options.put(key, name);
    }

    private Map<T, String> options;
    private T value;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<T, String> getOptions() {
        return options;
    }

    public void setOptions(Map<T, String> options) {
        this.options = options;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
