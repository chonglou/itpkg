package com.itpkg.email.dao;

import com.itpkg.email.models.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-18.
 */
@Repository("email.dao.doamin")
public interface DomainDao extends JpaRepository<Domain, Long> {
    Domain findByName(String name);

}
