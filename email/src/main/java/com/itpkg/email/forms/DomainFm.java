package com.itpkg.email.forms;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

/**
 * Created by flamen on 15-7-21.
 */
public class DomainFm {
    @NotEmpty
    @Range(min = 1, max = 64)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
