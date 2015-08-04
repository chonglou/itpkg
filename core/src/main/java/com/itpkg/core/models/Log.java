package com.itpkg.core.models;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "logs")
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Log implements Serializable {
    public enum Type {
        INFO, ERROR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(updatable = false)
    private User user;
    @Column(nullable = false, updatable = false)
    private String message;
    @Column(nullable = false, updatable = false)
    private Type type;
    @Column(nullable = false, updatable = false)
    private Date created;
}
