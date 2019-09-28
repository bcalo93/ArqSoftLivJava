package com.compucar.context;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@Import({CacheContext.class, ScheduleContext.class})
@EnableAspectJAutoProxy
@ComponentScan("com.compucar")
public class WebContext {
}
