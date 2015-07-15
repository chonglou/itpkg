package com.itpkg.core.models;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(length = 36)
    private long id;
    @Column(nullable = false)
    private Date created;
    @Column(nullable = false)
    private Date updated;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String providerId;
    @Column(nullable = false)
    private String providerUserId;
    private String accessToken;
    @JsonIgnore
    private String password;

    private Date confirmed;
    private Date locked;


    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    @OneToOne(mappedBy = "user")
    private Contact contact;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Log> logs;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Role> roles;

    public String getIds() {
        return Long.toString(id, 6);
    }

    public User() {
        this.logs = new ArrayList<>();
        this.roles = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public Date getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Date confirmed) {
        this.confirmed = confirmed;
    }

    public Date getLocked() {
        return locked;
    }

    public void setLocked(Date locked) {
        this.locked = locked;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
