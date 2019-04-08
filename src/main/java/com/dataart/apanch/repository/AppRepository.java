package com.dataart.apanch.repository;

import com.dataart.apanch.model.App;
import com.dataart.apanch.model.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppRepository extends PagingAndSortingRepository<App, Integer> {

    Page<App> findByCategoryType(CategoryType type, Pageable pageable);

    @Query("SELECT a FROM App a JOIN FETCH a.bigIcon WHERE a.id = (:id)")
    Optional<App> findById(@Param("id") Integer id);

    Optional<App> findByNameAndCategoryType(String name, CategoryType type);
}
