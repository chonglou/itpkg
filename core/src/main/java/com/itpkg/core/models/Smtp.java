package com.itpkg.core.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-8-3.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Smtp implements Serializable {
    public Smtp() {
        bcc = new ArrayList<>();
    }

    private String host;
    private int port;
    private boolean ssl;
    private String username;
    private String password;
    private List<String> bcc;
    private String from;


    @JsonIgnore
    public String[] getBccArray() {
        return bcc.toArray(new String[bcc.size()]);
    }

}
