package com.compucar.context;

import com.google.common.cache.CacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class MapTimeoutCacheManager extends ConcurrentMapCacheManager {
    private Long cacheTimeout;
    private Long maximumSize;
    private TimeUnit timeUnit;

    public void setCacheTimeout(Long value) {
        this.cacheTimeout = value;
    }

    public void setMaximumSize(Long value) {
        this.maximumSize = value;
    }

    public void setTimeUnit(TimeUnit value) {
        this.timeUnit = value;
    }

    @Override
    protected Cache createConcurrentMapCache(final String name) {
        ConcurrentMap map = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheTimeout, timeUnit)
                .maximumSize(maximumSize)
                .build()
                .asMap();
        return new ConcurrentMapCache(name, map, false);
    }

}
