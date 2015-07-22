package com.itpkg.core.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by flamen on 15-7-21.
 */
public class SignInFm {
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
