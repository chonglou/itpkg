package com.itpkg.core.dao;


import com.itpkg.core.models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by flamen on 15-7-14.
 */

public interface UserDao extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
