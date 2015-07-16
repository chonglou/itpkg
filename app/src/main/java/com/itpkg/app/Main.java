package com.itpkg.app;

import com.itpkg.core.web.Server;
import com.itpkg.core.web.impl.Jetty;

/**
 * Created by flamen on 15-7-16.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Jetty();
        server.init("development");
        server.start("localhost", 8080);
    }
}
