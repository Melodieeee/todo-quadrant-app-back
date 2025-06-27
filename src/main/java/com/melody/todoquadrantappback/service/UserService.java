package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.model.User;

public interface UserService {
    User getUserById(String id);

    User getUserByEmail(String email);
    User createUser(User user);

}
