package com.itpkg.email.models;

import com.itpkg.core.models.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by flamen on 15-7-18.
 */

@Entity(name = "EmailUser")
@Table(name = "email_users", indexes = {
        @Index(columnList = "email"),
        @Index(columnList = "email,domain_id", unique = true)
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
public class User extends IdEntity {
    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
}
