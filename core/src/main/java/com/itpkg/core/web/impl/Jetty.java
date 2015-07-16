package com.itpkg.core.web.impl;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by flamen on 15-7-16.
 */
public class Jetty extends com.itpkg.core.web.Server {
    private static final Logger logger = LoggerFactory.getLogger(Jetty.class);

    @Override
    public void start(String host, int port) throws Exception {

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(CONTEXT_PATH);
        handler.addServlet(new ServletHolder(new DispatcherServlet(context)), MAPPING_URL);
        handler.addEventListener(new ContextLoaderListener(context));

        server = new Server(port);
        server.setHandler(handler);

        server.start();
        server.join();

    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private Server server;

}
