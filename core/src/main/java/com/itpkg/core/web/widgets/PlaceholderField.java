package com.itpkg.core.web.widgets;

/**
 * Created by flamen on 15-7-20.
 */
public class PlaceholderField extends RequiredField {
    public PlaceholderField(String id, String type, String name) {
        super(id, type, name);
    }
    private String value;
    private String placeholder;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
