package com.compucar;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WebServer {

    private int port = 8080;
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
        WebAppContext jettyWebContext = new WebAppContext();
        // seteo del context path / path url escucha
        jettyWebContext.setContextPath(contextPath);

        // configuracion de tiempo de session
        jettyWebContext.getSessionHandler().getSessionManager().setMaxInactiveInterval(30 * 60); //Es en segundos

        // config spring web application context
        this.configSpring(jettyWebContext);

        // config web resources para jsps/html
        ClassPathResource classPathResource = new ClassPathResource("/webapp");
        String resourceBasePath = classPathResource.getURI().toString();
        jettyWebContext.setResourceBase(resourceBasePath);

        return jettyWebContext;
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

    public void setPort(int port) {
        this.port = port;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setSpringApplicationContextClass(Class<?> springApplicationContextClass) {
        this.springApplicationContextClass = springApplicationContextClass;
    }

    // Builder
    public WebServer contextPath(String path) {
        this.contextPath = path;
        return this;
    }

    public WebServer springApplicationContextClass(Class<?> springApplicationContextClass) {
        this.springApplicationContextClass = springApplicationContextClass;
        return this;
    }
}
