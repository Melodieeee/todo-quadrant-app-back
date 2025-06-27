package com.melody.todoquadrantappback.dao;

import com.melody.todoquadrantappback.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    User save(User user);
}
