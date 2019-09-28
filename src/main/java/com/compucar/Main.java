package com.compucar;

import com.compucar.context.WebContext;

public class Main {

    public static void main(String[] args) throws Exception {
        new WebServer()
            .contextPath("/app")
            .springApplicationContextClass(WebContext.class)
            .run();
    }
}
