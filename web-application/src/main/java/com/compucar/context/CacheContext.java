package com.compucar.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheContext {

    @Value("${cache.timeout}")
    private long cacheTimeout;

    @Value("${cache.maximunSize}")
    private long cacheMaximunSize;

    @Value("${cache.timeUnits}")
    private String timeUnits;


    @Bean
    public CacheManager cacheManager() {
        MapTimeoutCacheManager cacheManager = new MapTimeoutCacheManager();
        cacheManager.setCacheTimeout(cacheTimeout);
        cacheManager.setMaximumSize(cacheMaximunSize);
        cacheManager.setTimeUnit(TimeUnit.valueOf(timeUnits));
        return cacheManager;
    }
}