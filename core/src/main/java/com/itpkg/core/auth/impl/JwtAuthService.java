package com.itpkg.core.auth.impl;

import com.itpkg.core.auth.AuthService;
import com.itpkg.core.dao.RoleDao;
import com.itpkg.core.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by flamen on 15-7-20.
 */
@Service("core.auth.service.jwt")
public class JwtAuthService implements AuthService{
    @Override
    public boolean match(HttpServletRequest request) {
        return false;
    }

    @Autowired
    RoleDao roleDao;
    @Autowired
    UserDao userDao;


}
