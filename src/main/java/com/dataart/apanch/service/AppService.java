package com.dataart.apanch.service;

import com.dataart.apanch.model.App;
import com.dataart.apanch.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {
    @Autowired
    AppRepository appRepository;

    public List<App> findByCategoryId(Integer id){
        return appRepository.findByCategoryId(id);
    }
    
    public void save(App app){
        app.setDownloadsCount(0);
        appRepository.save(app);
    }

}
