package com.itpkg.core.auth;

import com.itpkg.core.models.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by flamen on 15-7-22.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Credential implements Serializable {
    public Credential() {
        params = new HashMap<>();
    }

    public Credential(User user, String action) {
        this();
        this.token = user.getAccessToken();
        this.provider = user.getProviderId();
        this.action = action;
    }

    private String token;
    private String provider;
    private String action;
    private Map<String, Object> params;

}
