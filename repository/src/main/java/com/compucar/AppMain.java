package com.compucar;

import com.compucar.context.WebContext;

public class AppMain {
    public static void main(String[] args) throws Exception {
        new WebServer().setContextPath("/repository")
                .setSpringApplicationContextClass(WebContext.class)
                .run();
    }
}
