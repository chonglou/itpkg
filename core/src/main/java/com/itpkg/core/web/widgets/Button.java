package com.itpkg.core.web.widgets;

import java.io.Serializable;

/**
 * Created by flamen on 15-7-17.
 */
public class Button implements Serializable {
    private String name;
    private String id;
    private String style;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
