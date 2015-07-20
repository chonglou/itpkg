package com.itpkg.email.dao;

import com.itpkg.email.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by flamen on 15-7-18.
 */
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    List<User> findByDomainId(long domainId);

  //  @Query("delete from EmailUser u where u.Domain.id = :domainId")
   void deleteByDomainId(@Param("domainId") long domainId);

}
