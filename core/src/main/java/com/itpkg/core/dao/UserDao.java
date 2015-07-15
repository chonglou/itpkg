package com.itpkg.core.dao;


import com.itpkg.core.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by flamen on 15-7-14.
 */

public interface UserDao extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
