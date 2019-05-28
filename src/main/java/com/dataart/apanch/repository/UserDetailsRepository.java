package com.dataart.apanch.repository;

import com.dataart.apanch.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserDetailsRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
