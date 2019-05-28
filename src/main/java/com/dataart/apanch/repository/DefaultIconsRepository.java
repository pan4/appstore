package com.dataart.apanch.repository;

import com.dataart.apanch.model.DefaultIcons;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefaultIconsRepository extends JpaRepository<DefaultIcons, Integer> {
    Optional<DefaultIcons> findFirstByOrderByIdDesc();
}
