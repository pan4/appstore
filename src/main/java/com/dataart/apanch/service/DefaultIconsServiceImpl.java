package com.dataart.apanch.service;

import com.dataart.apanch.model.DefaultIcons;
import com.dataart.apanch.repository.DefaultIconsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class DefaultIconsServiceImpl implements DefaultIconsService {
    @Autowired
    DefaultIconsRepository defaultIconsRepository;

    @Override
    public Optional<DefaultIcons> get() {
        return defaultIconsRepository.findFirstByOrderByIdDesc();
    }
}
