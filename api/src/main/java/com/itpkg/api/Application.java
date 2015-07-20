package com.itpkg.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by flamen on 15-7-20.
 */

@Configuration
@ComponentScan(basePackages = "com.itpkg")
@ImportResource("classpath*:spring/*.xml")
@EnableAutoConfiguration
public class Application {
}
