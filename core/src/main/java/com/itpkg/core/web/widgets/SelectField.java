package com.itpkg.core.web.widgets;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 15-7-18.
 */
public class SelectField<T> extends RequiredField<T> {
    public SelectField(String id, String name) {
        super(id, "select", name);
        options = new HashMap<>();
    }

    public void addOption(T key, String name) {
        options.put(key, name);
    }

    private Map<T, String> options;
    private T value;

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
