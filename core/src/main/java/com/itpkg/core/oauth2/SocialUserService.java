package com.itpkg.core.oauth2;

import com.itpkg.core.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * Created by flamen on 15-7-15.
 */
public interface SocialUserService extends SocialUserDetailsService, UserDetailsService {
    User loadUserByConnectionKey(ConnectionKey key);
    void updateUserDetails(User user);
}
