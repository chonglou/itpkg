package com.itpkg.email.models;

import com.itpkg.core.models.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by flamen on 15-7-18.
 */

@Entity(name = "EmailUser")
@Table(name = "email_users")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String email;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
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
}
