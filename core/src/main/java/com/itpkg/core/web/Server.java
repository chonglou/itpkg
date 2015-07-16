package com.itpkg.core.web;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Created by flamen on 15-7-16.
 */
public abstract class Server {

    public abstract void start();
    public abstract void stop() ;

    public void init(){
//        context = new AnnotationConfigWebApplicationContext();
//        context.setConfigLocation();
//        ClassPathXmlc
//                ((AbstractApplicationContext) context).registerShutdownHook();
        //protected WebApplicationContext context;
    }
    protected String host;
    protected int port;
    protected final String CONTEXT_PATH = "/";
    protected final String MAPPING_URL = "/*";
    protected final String DISPATCHER_NAME="dispatcher";


    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
