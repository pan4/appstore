package com.dataart.apanch.service;

import com.dataart.apanch.caching.CachingConfig;
import com.dataart.apanch.model.DefaultIcons;
import com.dataart.apanch.repository.DefaultIconsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DefaultIconsService {
    @Autowired
    DefaultIconsRepository defaultIconsRepository;

    @Cacheable(value = CachingConfig.DEFAULT_ICONS)
    public DefaultIcons get(){
        if(defaultIconsRepository.findAll().iterator().hasNext()){
            return defaultIconsRepository.findAll().iterator().next();
        }
        return null;
    }
}
