package com.itpkg;

import com.itpkg.core.services.SettingService;
import com.itpkg.core.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    FilterRegistrationBean corsFilter(
            @Value("${tagit.origin:http://localhost:8080}") String origin) {
        return new FilterRegistrationBean(new Filter() {
            @Override
            public void init(FilterConfig filterConfig) throws ServletException {

            }

            @Override
            public void doFilter(ServletRequest req, ServletResponse res,
                                 FilterChain chain) throws IOException, ServletException {
                HttpServletRequest request = (HttpServletRequest) req;
                HttpServletResponse response = (HttpServletResponse) res;
                String method = request.getMethod();

                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Methods",
                        "POST,GET,PUT,OPTIONS,DELETE");
                response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
                response.setHeader("Access-Control-Allow-Credentials", "true");
                response.setHeader(
                        "Access-Control-Allow-Headers",
                        "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
                if ("OPTIONS".equals(method)) {
                    response.setStatus(HttpStatus.OK.value());
                } else {
                    chain.doFilter(req, res);
                }
            }

            @Override
            public void destroy() {

            }


        });
    }

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

