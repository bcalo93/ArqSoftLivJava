package com.compucar.context;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan("com.compucar")
@Import({PropertiesContext.class, MessagingContext.class, ClientConfig.class})
public class WebContext {
}
