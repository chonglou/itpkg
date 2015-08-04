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
@Entity(name = "EmailAlias")
@Table(name = "email_aliases", indexes = {
        @Index(columnList = "source", unique = true),
        @Index(columnList = "destination")
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)

@Data
@EqualsAndHashCode(callSuper = false)
public class Alias extends IdEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;
    @Column(nullable = false, unique = true)
    private String source;
    @Column(nullable = false)
    private String destination;


}
