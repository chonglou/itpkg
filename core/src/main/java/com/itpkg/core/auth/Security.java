package com.itpkg.core.auth;

import com.itpkg.core.services.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Created by flamen on 15-7-27.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider)
           //     .authenticationProvider(anonymousAuthenticationProvider())
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(
                new AuthenticationFilter(authenticationManager(), i18n),
                BasicAuthenticationFilter.class)
          //      .addFilterAfter(anonymousAuthenticationFilter(), BasicAuthenticationFilter.class)
                .csrf().disable();
    }

//    @Bean
//    public AnonymousAuthenticationFilter anonymousAuthenticationFilter(){
//        return new AnonymousAuthenticationFilter("anonymous");
//    }
//    @Bean
//    public AnonymousAuthenticationProvider anonymousAuthenticationProvider(){
//        return new AnonymousAuthenticationProvider("anonymous");
//    }

    @Autowired
    @Qualifier("core.authenticationProvider")
    AuthenticationProvider authenticationProvider;
    @Autowired
    I18nService i18n;
}
