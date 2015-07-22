package com.itpkg.email.models;

import com.itpkg.core.models.IdEntity;
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
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Alias extends IdEntity {

    @ManyToOne
    @JoinColumn(nullable = false)
    private Domain domain;
    @Column(nullable = false, unique = true)
    private String source;
    @Column(nullable = false)
    private String destination;

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
