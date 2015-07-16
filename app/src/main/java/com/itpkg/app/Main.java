package com.itpkg.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by flamen on 15-7-16.
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // AnnotationConfigApplicationContext(Application.class);
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:spring/*.xml");
        ctx.registerShutdownHook();

    }
}
