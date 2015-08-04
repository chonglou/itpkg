package com.itpkg.core.models;

import java.io.Serializable;
import java.util.*;

/**
 * Created by flamen on 15-8-3.
 */
public class Mail implements Serializable {
    public Mail() {
        this.cc = new ArrayList<>();
        this.bcc = new ArrayList<>();
        this.to = new ArrayList<>();
        this.files = new LinkedHashMap<>();
        this.created = new Date();
    }


    private String from;
    private List<String> cc;
    private List<String> bcc;
    private List<String> to;
    private Map<String, byte[]> files;

    private String subject;
    private String body;
    private boolean html;
    private Date created;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getBcc() {
        return bcc;
    }

    public Map<String, byte[]> getFiles() {
        return files;
    }

    public void setFiles(Map<String, byte[]> files) {
        this.files = files;
    }

    public void setBcc(List<String> bcc) {
        this.bcc = bcc;
    }

    public List<String> getTo() {
        return to;
    }

    public void setTo(List<String> to) {
        this.to = to;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
