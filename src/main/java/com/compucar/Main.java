package com.compucar;

import com.compucar.context.WebContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class Main {

    @Autowired
    private static ApplicationContext ctx;

    public static void main(String[] args) throws Exception {
        new WebServer()
                .contextPath("/app")
                .springApplicationContextClass(WebContext.class)
                .run();
    }
}