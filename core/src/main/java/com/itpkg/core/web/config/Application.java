package com.itpkg.core.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;

/**
 * Created by flamen on 15-7-16.
 */
@Configuration
@ComponentScan(basePackages = "com.itpkg")
@ImportResource("classpath*:spring/*.xml")
public class Application {

}
