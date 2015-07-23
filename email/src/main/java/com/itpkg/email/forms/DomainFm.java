package com.itpkg.email.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

/**
 * Created by flamen on 15-7-21.
 */
public class DomainFm {
    @NotEmpty
    @Size(min = 1, max = 64)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
