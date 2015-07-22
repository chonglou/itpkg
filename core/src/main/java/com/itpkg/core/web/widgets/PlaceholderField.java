package com.itpkg.core.web.widgets;

/**
 * Created by flamen on 15-7-20.
 */
public class PlaceholderField extends RequiredField<String> {
    public PlaceholderField(String id, String type, String name) {
        super(id, type, name);
    }

    private String placeholder;


    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
