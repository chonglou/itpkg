package com.itpkg.email.models;

import com.itpkg.core.models.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-18.
 */

@Entity
@Table(name = "email_domains")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Domain extends IdEntity {
    public Domain() {
        users = new ArrayList<>();
        aliases = new ArrayList<>();
    }

    @Column(nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "domain")
    private List<User> users;
    @OneToMany(mappedBy = "domain")
    private List<Alias> aliases;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Alias> getAliases() {
        return aliases;
    }

    public void setAliases(List<Alias> aliases) {
        this.aliases = aliases;
    }
}
