package com.itpkg;

import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

import java.util.Arrays;

/**
 * Created by flamen on 15-7-14.
 */

@Configuration
@ComponentScan
@PropertySource("classpath:config.properties")
@ImportResource("classpath:spring/*.xml")
@EnableAutoConfiguration
public class Application {
    private final Logger logger = LoggerFactory.getLogger(Application.class);

    @Bean
    CommandLineRunner init(UserService userService,
                           SettingService settingService) {
        logger.info("Start application");
        return (evt) -> Arrays.asList(
                "root,admin".split(","))
                .forEach(
                        a -> {

                        });
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


}
