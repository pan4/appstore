package com.dataart.apanch.repository;

import com.dataart.apanch.model.AppPackage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AppPackageRepository extends CrudRepository<AppPackage, Integer> {
    Optional<AppPackage> findByAppId(Integer id);
}
