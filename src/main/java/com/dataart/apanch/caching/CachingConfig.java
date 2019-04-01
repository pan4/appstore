package com.dataart.apanch.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


import java.time.LocalDate;

@Configuration
@EnableCaching
@EnableScheduling
public class CachingConfig {

    public static final String POPULAR = "POPULAR";
    public static final String CATEGORIES = "CATEGORIES";
    public static final String DEFAULT_ICONS = "DEFAULT_ICONS";

    private static final Logger logger = LoggerFactory.getLogger(CachingConfig.class);

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(POPULAR, CATEGORIES, DEFAULT_ICONS);
    }

    @CacheEvict(allEntries = true, value = {POPULAR})
    @Scheduled(fixedDelay = 1 * 60 * 1000, initialDelay = 500)
    public void reportCacheEvict() {
        logger.info("Flush Cache " + LocalDate.now());
    }
}
