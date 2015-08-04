package com.itpkg.core.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by flamen on 15-7-16.
 */
@MappedSuperclass
@Data
public class IdEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, updatable = false)
    private Date created;
    @Column(nullable = false)
    private Date updated;

}
