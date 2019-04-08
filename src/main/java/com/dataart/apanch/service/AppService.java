package com.dataart.apanch.service;

import com.dataart.apanch.caching.CachingConfig;
import com.dataart.apanch.model.App;
import com.dataart.apanch.model.CategoryType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AppService {
    Page<App> findByCategoryType(CategoryType type, Pageable pageable);

    Optional<App> findById(Integer id);

    @Cacheable(value = CachingConfig.POPULAR)
    List<App> findPopular();

    boolean trySave(MultipartFile file, App app, BindingResult result) throws IOException;

    boolean isAppUnique(App app);
}
