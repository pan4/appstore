package com.dataart.apanch.repository;

import com.dataart.apanch.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDetailsRepository extends CrudRepository<User, Integer> {
    User findByUsername(String username);
}
