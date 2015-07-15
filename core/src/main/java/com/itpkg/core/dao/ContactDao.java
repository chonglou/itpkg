package com.itpkg.core.dao;

import com.itpkg.core.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by flamen on 15-7-15.
 */
public interface ContactDao extends JpaRepository<Contact, Long> {
}
