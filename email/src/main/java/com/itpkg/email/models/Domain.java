package com.itpkg.email.models;

import com.itpkg.core.models.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

@Entity(name = "EmailDomain")
@Table(name = "email_domains")
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)

@Data
@EqualsAndHashCode(callSuper = false)
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


}
