package com.itpkg.core.auth;

import com.itpkg.core.models.User;
import com.itpkg.core.services.I18nService;
import com.itpkg.core.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by flamen on 15-7-27.
 */
@Component("core.authenticationProvider")
@Slf4j
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String ticket = (String) authentication.getPrincipal();

        try {
            Credential ut = jwtHelper.token2payload(ticket, Credential.class);
            User user = userService.findByToken(ut.getProvider(), ut.getToken());
            if (user == null) {
                throw new BadCredentialsException(i18n.T("errors.user.bad_token"));
            }
            List<GrantedAuthority> auths = new ArrayList<>();
            //todo
            return new UsernamePasswordAuthenticationToken(ut.getProvider(), ut.getToken(), auths);


        } catch (InvalidJwtException | MalformedClaimException e) {
            throw new BadCredentialsException(i18n.T("errors.user.bad_token"), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.equals(authentication);
    }

    @Autowired
    UserService userService;
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    I18nService i18n;
}
