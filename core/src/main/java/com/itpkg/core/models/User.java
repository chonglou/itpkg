package com.itpkg.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "users", indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "username"),
        @Index(columnList = "providerId,providerUserId", unique = true)
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends IdEntity {

    public boolean isLocked() {
        return locked != null;
    }

    public boolean isConfirmed() {
        return confirmed != null;
    }

    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false, updatable = false)
    private String providerId;
    @Column(nullable = false)
    private String providerUserId;
    @Column(nullable = false)
    private String accessToken;
    private String password;

    private Date confirmed;
    private Date locked;
    @OneToOne(mappedBy = "user")
    private Contact contact;

    @OneToMany(mappedBy = "user")
    private List<Log> logs;

    @OneToMany(mappedBy = "user")
    private List<Role> roles;

}
