package com.itpkg.core.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by flamen on 15-7-16.
 */
@Configuration
@ComponentScan(basePackages = "com.itpkg")
@ImportResource("classpath*:spring/*.xml")
public class Application {
//    @Bean
//    Filter encodingFilter(){
//        CharacterEncodingFilter f = new CharacterEncodingFilter();
//        f.setEncoding("UTF-8");
//        f.setForceEncoding(true);
//        return f;
//    }

}
