package com.itpkg.email.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by flamen on 15-7-21.
 */
public class AliasFm {
    @NotEmpty
    private long domain;
    @NotEmpty
    @Size(min = 1, max = 64)
    private String source;
    @NotEmpty
    @Size(min = 1, max = 64)
    private String destination;

    public long getDomain() {
        return domain;
    }

    public void setDomain(long domain) {
        this.domain = domain;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
