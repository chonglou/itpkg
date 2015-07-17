package com.itpkg.app.impl;

import com.itpkg.app.Server;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by flamen on 15-7-16.
 */
public class Undertow extends Server {
    private static final Logger logger = LoggerFactory.getLogger(Undertow.class);

    @Override
    public void start(String host, int port) {
        logger.info("Start application....");
        undertow = io.undertow.Undertow.builder().addHttpListener(port, host).setHandler(new HttpHandler() {
            @Override
            public void handleRequest(HttpServerExchange exchange) throws Exception {
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
                exchange.getResponseSender().send("Hello World");
            }
        }).build();
        undertow.start();
    }

    @Override
    public void stop() {
        undertow.stop();
        logger.info("Stop application!!!");
    }

    private io.undertow.Undertow undertow;


}
