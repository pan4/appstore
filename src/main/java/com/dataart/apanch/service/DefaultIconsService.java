package com.dataart.apanch.service;

import com.dataart.apanch.caching.CachingConfig;
import com.dataart.apanch.model.DefaultIcons;
import org.springframework.cache.annotation.Cacheable;

import java.util.Optional;

public interface DefaultIconsService {
    @Cacheable(value = CachingConfig.DEFAULT_ICONS)
    Optional<DefaultIcons> get();
}
