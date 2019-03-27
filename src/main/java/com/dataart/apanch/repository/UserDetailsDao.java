package com.dataart.apanch.repository;

import com.dataart.apanch.model.User;

public interface UserDetailsDao {
  User findUserByUsername(String username);
}
