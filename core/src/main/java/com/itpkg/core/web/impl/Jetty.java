package com.itpkg.core.web.impl;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Created by flamen on 15-7-16.
 */
public class Jetty extends com.itpkg.core.web.Server {
    private static final Logger logger = LoggerFactory.getLogger(Jetty.class);
    @Override
    public void start(){
        ServletHolder holder = new ServletHolder(DISPATCHER_NAME, new DispatcherServlet());
        holder.setInitParameter("contextConfigLocation", "classpath*:spring/*.xml");

        ServletContextHandler handler = new ServletContextHandler();
        handler.setContextPath(CONTEXT_PATH);
        handler.addServlet(holder, MAPPING_URL);
        
        //handler.addEventListener();

        server = new Server(port);
        server.setHandler(handler);
        try {
            server.start();
            server.join();
        }
        catch (InterruptedException e){
            logger.info("Exit...");
        }
        catch (Exception e){
            logger.error("Start jetty failed!", e);
        }
    }

    @Override
    public void stop(){
        try{
            server.stop();
        }
        catch (Exception e){
            logger.error("Stop jetty failed!", e);
        }
    }

    private Server server;

}
