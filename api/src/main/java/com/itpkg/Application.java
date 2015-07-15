package com.itpkg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by flamen on 15-7-14.
 */

@Configuration
@ComponentScan
@EnableAutoConfiguration
public class Application {
    @Bean
    CommandLineRunner init() {
        return (evt) -> Arrays.asList(
                "root,admin".split(",")).forEach(
                a -> {
                    //todo 添加用户111
                });
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
