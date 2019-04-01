package com.dataart.apanch.service;

import com.dataart.apanch.model.DefaultIcons;
import com.dataart.apanch.repository.DefaultIconsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultIconsServiceImpl implements DefaultIconsService {
    @Autowired
    DefaultIconsRepository defaultIconsRepository;

    @Override
    public DefaultIcons get(){
        if(defaultIconsRepository.findAll().iterator().hasNext()){
            return defaultIconsRepository.findAll().iterator().next();
        }
        return null;
    }
}
