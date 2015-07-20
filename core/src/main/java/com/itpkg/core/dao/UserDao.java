package com.itpkg.core.dao;


import com.itpkg.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by flamen on 15-7-14.
 */
@Repository("core.dao.user")
public interface UserDao extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
