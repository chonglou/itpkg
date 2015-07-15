package com.itpkg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

/**
 * Created by flamen on 15-7-15.
 */


@Configuration
@EnableResourceServer
@EnableAuthorizationServer
class OAuth2 extends AuthorizationServerConfigurerAdapter {


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints.authenticationManager(auth -> authenticationManager.getOrBuild().authenticate(auth));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory().withClient("android-" + applicationName)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_USER").scopes("write").resourceIds(applicationName)
                .secret("123456");
    }

    @Autowired
    private AuthenticationManagerBuilder authenticationManager;
    private String applicationName = "it-package"; //todo 从数据库中读取

    public void setAuthenticationManager(AuthenticationManagerBuilder authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}