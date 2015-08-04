package com.itpkg.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "roles", indexes = {
        @Index(columnList = "name"),
        @Index(columnList = "rType"),
        @Index(columnList = "name,rtype,rid,user_id", unique = true)
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
public class Role extends IdEntity {

    @ManyToOne
    @JoinColumn
    private User user;
    @Column(nullable = false)
    private String name;
    private String rType;
    private Integer rId;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startUp;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date shutDown;

}
