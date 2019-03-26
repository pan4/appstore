package com.dataart.apanch.repository;

import com.dataart.apanch.model.AppPackage;
import org.springframework.data.repository.CrudRepository;

public interface AppPackageRepository extends CrudRepository<AppPackage, Integer> {
    AppPackage findByAppId(Integer id);
}
