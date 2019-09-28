package com.compucar.context;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheContext {

    // TODO - Move hardcoded variables to a properties file
    private long cacheTimeout = 5;
    private long cacheMaximunSize = 100;
    private String timeUnits = "MINUTES";


    @Bean
    public CacheManager cacheManager() {
        MapTimeoutCacheManager cacheManager = new MapTimeoutCacheManager();
        cacheManager.setCacheTimeout(cacheTimeout);
        cacheManager.setMaximumSize(cacheMaximunSize);
        cacheManager.setTimeUnit(TimeUnit.valueOf(timeUnits));
        return cacheManager;
    }

}
