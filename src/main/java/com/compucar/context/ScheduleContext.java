package com.compucar.context;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@EnableScheduling
public class ScheduleContext {

    // TODO - Move hardcoded variables to a properties file
    private int poolSize = 5;
    private String poolPrefix = "Sch-";

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.setThreadNamePrefix(poolPrefix);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
