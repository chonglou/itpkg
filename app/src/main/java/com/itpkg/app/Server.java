package com.itpkg.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Created by flamen on 15-7-16.
 */
public abstract class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public abstract void start(String host, int port) throws Exception;

    public abstract void stop() throws Exception;

    public void init(String env) {
        context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.itpkg.core.config");
        context.getEnvironment().setDefaultProfiles(env);
        context.registerShutdownHook();
    }

    protected final String CONTEXT_PATH = "/";
    protected final String MAPPING_URL = "/*";
    protected AnnotationConfigWebApplicationContext context;

}
