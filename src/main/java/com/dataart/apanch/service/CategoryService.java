package com.dataart.apanch.service;

import com.dataart.apanch.caching.CachingConfig;
import com.dataart.apanch.model.Category;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    @Cacheable(value = CachingConfig.CATEGORIES)
    List<Category> findAll();

    Optional<Category> findById(Integer id);
}
