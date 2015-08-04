package com.itpkg.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.*;

/**
 * Created by flamen on 15-8-3.
 */
@Data
@EqualsAndHashCode(callSuper = false)
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

}
