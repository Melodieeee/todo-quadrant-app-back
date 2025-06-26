package com.melody.mytodoquadrantappback.dao;

import com.melody.mytodoquadrantappback.model.User;

import java.util.Optional;

public interface UserDao {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);
    User save(User user);
}
