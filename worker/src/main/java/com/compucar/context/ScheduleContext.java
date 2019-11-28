package com.compucar.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleContext {
    @Value("${schedule.poolSize}")
    private int poolSize;

    @Value("${schedule.poolPrefix}")
    private String poolPrefix;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(poolSize);
        taskScheduler.setThreadNamePrefix(poolPrefix);
        taskScheduler.initialize();
        return taskScheduler;
    }
}
