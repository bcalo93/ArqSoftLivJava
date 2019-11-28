package com.compucar;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebServer {
    private int port;
    private String contextPath = "/";
    private Class<?> springApplicationContextClass;

    private Server server;

    public WebServer() {
    }

    public WebServer(int port, String contextPath, Class<?> springApplicationContextClass) {
        this.port = port;
        this.contextPath = contextPath;
        this.springApplicationContextClass = springApplicationContextClass;
    }

    public void run() throws Exception {
        this.server = new Server(port);

        ServletContextHandler jettyContext = this.buildJettyContext();
        this.server.setHandler(jettyContext);
        this.server.setStopAtShutdown(true);

        this.server.start();
        this.server.join();
    }

    private ServletContextHandler buildJettyContext() throws Exception {
        ServletContextHandler jettyContext = new ServletContextHandler();
        jettyContext.setContextPath(contextPath);

        this.configSpring(jettyContext);

        return jettyContext;
    }

    private void configSpring(ServletContextHandler jettyContext) {
        // creo el application context, basado en clase de configuracion
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(this.springApplicationContextClass);

        // crear dispatcher servlet de spring
        DispatcherServlet dispatcherServlet = new DispatcherServlet(applicationContext);
        ServletHolder dispatcherServletHolder = new ServletHolder(dispatcherServlet);
        jettyContext.addServlet(dispatcherServletHolder, "/");

        // agregar el application context de spring a la aplicacion web
        ContextLoaderListener contextLoaderListener = new ContextLoaderListener(applicationContext);
        jettyContext.addEventListener(contextLoaderListener);
        applicationContext.close();
    }

    public WebServer setPort(int port) {
        this.port = port;
        return this;
    }

    public WebServer setContextPath(String contextPath) {
        this.contextPath = contextPath;
        return this;
    }

    public WebServer setSpringApplicationContextClass(Class<?> springApplicationContextClass) {
        this.springApplicationContextClass = springApplicationContextClass;
        return this;
    }
}
