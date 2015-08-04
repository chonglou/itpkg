package com.itpkg.core.auth;

import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

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
            log.debug("get ticket: ", ticket);
            UserToken tk = jwtHelper.token2payload(ticket, UserToken.class);
            return tokenService.retrieve(tk.getTid());

        } catch (InvalidJwtException | MalformedClaimException e) {
            log.error("parse token error", e);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PreAuthenticatedAuthenticationToken.class.equals(authentication);
    }

    @Autowired
    TokenService tokenService;
    @Autowired
    JwtHelper jwtHelper;
}
