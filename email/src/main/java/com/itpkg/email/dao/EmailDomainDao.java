package com.itpkg.email.dao;

import com.itpkg.email.models.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by flamen on 15-7-18.
 */
public interface EmailDomainDao extends JpaRepository<Domain, Long> {
    Optional<Domain> findByName(String name);

}
