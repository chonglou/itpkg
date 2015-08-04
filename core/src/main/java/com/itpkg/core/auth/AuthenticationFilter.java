package com.itpkg.core.auth;

import com.itpkg.core.services.I18nService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
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

    public AuthenticationFilter(AuthenticationManager authenticationManager, I18nService i18n) {
        this.i18n = i18n;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        String ticket = getTicket(req);
        if (ticket != null) {
            try {
                Authentication reqAuth = new PreAuthenticatedAuthenticationToken(ticket, null);
                Authentication respAuth = authenticationManager.authenticate(reqAuth);
//                if (respAuth == null || !respAuth.isAuthenticated()) {
//                    throw new InternalAuthenticationServiceException(i18n.T("errors.user.bad_token"));
//                }
                if (respAuth != null && respAuth.isAuthenticated()) {
                    SecurityContextHolder.getContext().setAuthentication(respAuth);
                }

            } catch (AuthenticationException e) {
                logger.error("auth failed", e);
                SecurityContextHolder.clearContext();
                //resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        chain.doFilter(request, response);
    }

    private String getTicket(HttpServletRequest request) {
        String ticket = request.getParameter("ticket");
        if (ticket == null) {
            String auth = request.getHeader("Authorization");
            if (auth != null) {
                String[] ss = auth.split(" ");
                if (ss.length == 2 && "Bearer".equals(ss[0])) {
                    ticket = ss[1];
                }
            }
        }

        if ("null".equals(ticket)) {
            ticket = null;
        }

        return ticket;
    }


    private I18nService i18n;
    private AuthenticationManager authenticationManager;
}
