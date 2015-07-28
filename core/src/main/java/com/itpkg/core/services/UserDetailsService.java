package com.itpkg.core.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by flamen on 15-7-27.
 */

@Service("core.userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final static Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("TODO: user details");
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER1111"));
        UserDetails ud = new User(username, "password", true, true, true, true, authorities);
        return ud;
    }
}
