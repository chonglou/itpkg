package com.itpkg.core.auth;

import com.itpkg.core.services.I18nService;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by flamen on 15-7-27.
 */

public class AuthenticationFilter extends GenericFilterBean {
    private final static Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtHelper jwtHelper, I18nService i18n) {
        this.i18n = i18n;
        this.jwtHelper = jwtHelper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        String jwt = jwt(req);
        if (jwt != null) {
            try {
                logger.debug("get jwt: " + jwt);
                Authentication reqAuth = new PreAuthenticatedAuthenticationToken(jwtHelper.token2payload(jwt, String.class), null);
                Authentication respAuth = authenticationManager.authenticate(reqAuth);
                if (respAuth == null || !respAuth.isAuthenticated()) {
                    throw new InternalAuthenticationServiceException(i18n.T("errors.user.bad_token"));
                }

                SecurityContextHolder.getContext().setAuthentication(respAuth);

            } catch (AuthenticationException | InvalidJwtException | MalformedClaimException e) {
                logger.error("auth failed", e);
                SecurityContextHolder.clearContext();
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        chain.doFilter(request, response);
    }

    private String jwt(HttpServletRequest request) {
        String token = request.getParameter("token");
        if (token == null) {

            String auth = request.getHeader("Authorization");
            if (auth != null) {
                String[] ss = auth.split(" ");
                if (ss.length == 2 && !"Bearer".equals(ss[0])) {
                    token = ss[1];
                }
            }
        }


        return token;
    }


    private I18nService i18n;
    private JwtHelper jwtHelper;
    private AuthenticationManager authenticationManager;
}