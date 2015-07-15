package com.itpkg.core.services;

import com.itpkg.core.dao.UserDao;
import com.itpkg.core.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by flamen on 15-7-14.
 */
@Service
public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public void Create(String username, String email, String password) {

    }

    public void Create(User.Provider provider, String username, String email) {

    }

    @Resource
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
