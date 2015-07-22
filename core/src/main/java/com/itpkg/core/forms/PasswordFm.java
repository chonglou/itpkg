package com.itpkg.core.forms;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by flamen on 15-7-22.
 */
public class PasswordFm implements Serializable {
    @NotEmpty
    private String token;
    @NotEmpty
    @Size(min = 6, max = 128)
    private String password;
    private String passwordConfirm;

    @AssertTrue(message = "passwords not match")
    public boolean isValid() {
        return password.equals(passwordConfirm);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
