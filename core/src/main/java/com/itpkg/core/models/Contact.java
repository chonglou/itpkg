package com.itpkg.core.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by flamen on 15-7-14.
 */
@Entity
@Table(name = "contacts")
@Cache(region = "root", usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@EqualsAndHashCode(callSuper = false)
public class Contact extends IdEntity {

    @OneToOne
    @JoinColumn
    private User user;
    private String qq;
    private String skype;
    private String weChat;
    private String linkedIn;
    private String factBook;
    private String email;
    private String logo;
    private String phone;
    private String tel;
    private String fax;
    private String address;
    @Lob
    private String details;

}
