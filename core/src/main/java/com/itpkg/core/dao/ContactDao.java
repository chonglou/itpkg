package com.itpkg.core.dao;

import com.itpkg.core.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-15.
 */
@Repository("core.dao.contact")
public interface ContactDao extends JpaRepository<Contact, Long> {
}
