package com.itpkg.email.dao;

import com.itpkg.email.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by flamen on 15-7-18.
 */
@Repository("email.dao.user")
public interface UserDao extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findByDomainId(long domainId);

    void deleteByDomainId(long domainId);

}
