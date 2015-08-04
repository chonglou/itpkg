package com.itpkg.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Created by flamen on 15-7-14.
 */

@Entity
@Table(name = "settings")
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
public class Setting extends IdEntity {


    @Column(nullable = false, unique = true, name = "key_")
    private String key;

    @Column(nullable = false)
    @Lob
    private String val;

    private boolean encode;

}
