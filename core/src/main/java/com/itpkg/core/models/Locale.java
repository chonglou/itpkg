package com.itpkg.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by flamen on 15-7-21.
 */

@Entity
@Table(name = "locales", indexes = {
        @Index(columnList = "code"),
        @Index(columnList = "lang"),
        @Index(columnList = "code,lang", unique = true)
})
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
public class Locale extends IdEntity {
    @Column(nullable = false)
    private String code;
    @Column(nullable = false, length = 5)
    private String lang;
    @Lob
    private String message;

}
