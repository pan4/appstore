package com.dataart.apanch.repository;

import com.dataart.apanch.model.App;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AppRepository extends CrudRepository<App, Integer> {
    List<App> findByCategoryId(Integer id);
}
