package com.compucar;

import com.compucar.context.WebContext;

public class AppMain {
    public static void main(String[] args) throws Exception {
        new WebServer().setContextPath("/worker")
                .setPort(8083)
                .setSpringApplicationContextClass(WebContext.class)
                .run();

    }
}
