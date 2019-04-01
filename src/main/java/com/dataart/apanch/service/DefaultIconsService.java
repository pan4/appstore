package com.dataart.apanch.service;

import com.dataart.apanch.caching.CachingConfig;
import com.dataart.apanch.model.DefaultIcons;
import org.springframework.cache.annotation.Cacheable;

public interface DefaultIconsService {
    @Cacheable(value = CachingConfig.DEFAULT_ICONS)
    DefaultIcons get();
}
