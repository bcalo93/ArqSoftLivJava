package com.compucar;

import com.compucar.context.WebContext;

public class AppMain {
    public static void main(String[] args) throws Exception {
        new WebServer().setContextPath("/repo")
                .setSpringApplicationContextClass(WebContext.class)
                .run();
    }
}
