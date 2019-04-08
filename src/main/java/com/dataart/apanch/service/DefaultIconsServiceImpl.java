package com.dataart.apanch.service;

import com.dataart.apanch.model.DefaultIcons;
import com.dataart.apanch.repository.DefaultIconsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultIconsServiceImpl implements DefaultIconsService {
    @Autowired
    DefaultIconsRepository defaultIconsRepository;

    @Override
    public Optional<DefaultIcons> get() {
        List<DefaultIcons> all = defaultIconsRepository.findAll();
        if (all.size() == 1) {
            return Optional.of(all.get(0));
        }
        return Optional.empty();
    }
}
