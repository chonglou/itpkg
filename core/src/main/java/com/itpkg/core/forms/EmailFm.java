package com.itpkg.core.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by flamen on 15-7-21.
 */
public class EmailFm {
    @NotEmpty
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
