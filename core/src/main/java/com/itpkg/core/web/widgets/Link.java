package com.itpkg.core.web.widgets;

import java.io.Serializable;

/**
 * Created by flamen on 15-7-21.
 */
public class Link implements Serializable {
    public Link() {
    }

    public Link(String url, String name) {
        this.url = url;
        this.name = name;
    }

    private String url;
    private String name;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
