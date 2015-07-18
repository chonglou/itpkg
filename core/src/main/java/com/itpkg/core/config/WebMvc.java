package com.itpkg.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by flamen on 15-7-16.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.itpkg")
@ImportResource("classpath*:spring/*.xml")
public class WebMvc extends WebMvcConfigurerAdapter {
}
