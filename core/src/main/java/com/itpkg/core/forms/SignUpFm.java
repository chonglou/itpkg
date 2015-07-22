package com.itpkg.core.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.AssertTrue;

/**
 * Created by flamen on 15-7-21.
 */
public class SignUpFm {
    @NotEmpty
    @Range(min = 2, max = 32)
    private String username;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Range(min = 6, max = 128)
    private String password;
    private String passwordConfirm;

    @AssertTrue(message = "passwords not match")
    public boolean isValid() {
        return password.equals(passwordConfirm);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
