package com.itpkg.core.auth;

import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

/**
 * Created by flamen on 15-7-27.
 */
@Component("core.authenticationProvider")
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String ticket = (String) authentication.getPrincipal();

        try {
            logger.debug("get ticket: ", ticket);
            UserToken tk = jwtHelper.token2payload(ticket, UserToken.class);
            return tokenService.retrieve(tk.getTid());

        } catch (InvalidJwtException | MalformedClaimException e) {
            logger.error("parse token error", e);
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
