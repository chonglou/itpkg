package com.itpkg.email.dao;

import com.itpkg.email.models.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Created by flamen on 15-7-18.
 */
public interface AliasDao extends JpaRepository<Alias, Long> {
    Optional<Alias> findBySource(String source);

    @Query("delete from EmailAlias a where a.Domain.id = :domainId")
    void deleteByDomain(@Param("domainId") long domainId);

    void deleteByDestination(String destination);
}
