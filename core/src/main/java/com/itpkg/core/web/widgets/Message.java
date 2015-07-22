package com.itpkg.core.web.widgets;

import java.io.Serializable;

/**
 * Created by flamen on 15-7-22.
 */
public class Message implements Serializable {
    public static Message Warning(String subject, String body, Link link) {
        return new Message("warning", subject, body, link);
    }

    public static Message Success(String subject, String body, Link link) {
        return new Message("success", subject, body, link);
    }

    public static Message Error(String subject, String body, Link link) {
        return new Message("danger", subject, body, link);
    }

    public Message() {
    }

    public Message(String style, String subject, String body, Link link) {
        this.body = body;
        this.subject = subject;
        this.style = style;
        this.link = link;
    }

    private String body;
    private String subject;
    private String style;
    private Link link;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
