package com.dataart.apanch.repository;

import com.dataart.apanch.model.DefaultIcons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DefaultIconsRepository extends JpaRepository<DefaultIcons, Integer> {
}
