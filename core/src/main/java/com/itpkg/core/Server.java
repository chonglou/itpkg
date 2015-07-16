package com.itpkg.core;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by flamen on 15-7-16.
 */
@Component
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    @PreDestroy
    void destroy() {
        undertow.stop();
        logger.info("Stop application!!!");
    }

    @PostConstruct
    void init() {
        logger.info("Start application....");
        undertow = Undertow.builder().addHttpListener(8080, "localhost").setHandler(new HttpHandler() {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws Exception {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send("Hello World");
            }
        }).build();
        undertow.start();
    }

    private Undertow undertow;
}
