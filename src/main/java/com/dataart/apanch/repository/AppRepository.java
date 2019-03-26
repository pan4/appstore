package com.dataart.apanch.repository;

import com.dataart.apanch.model.App;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface AppRepository extends PagingAndSortingRepository<App, Integer> {

    Page<App> findByCategoryId(Integer id, Pageable pageable);

    @Query("SELECT a FROM App a JOIN FETCH a.bigIcon WHERE a.id = (:id)")
    App findById(@Param("id") Integer id);
}
