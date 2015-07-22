package com.itpkg.email.dao;

import com.itpkg.email.models.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by flamen on 15-7-18.
 */
@Repository("email.dao.alias")
public interface EmailAliasDao extends JpaRepository<Alias, Long> {
    Alias findBySource(String source);

    //@Query("delete from EmailAlias a where a.Domain.id = :domainId")
    //void deleteByDomainId(@Param("domainId") long domainId);
    void deleteByDomainId(long domainId);

    void deleteByDestination(String destination);
}
