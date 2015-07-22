package com.itpkg.email.forms;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

/**
 * Created by flamen on 15-7-21.
 */
public class UserFm {
    @NotEmpty
    @Size(min = 1, max = 64)
    private String username;
    @NotEmpty
    private long domain;
    @NotEmpty
    @Size(min = 6, max = 128)
    private String password;
    private String passwordConfirm;

    @AssertTrue
    public boolean isValid() {
        return password!=null && password.equals(passwordConfirm);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getDomain() {
        return domain;
    }

    public void setDomain(long domain) {
        this.domain = domain;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
