package com.melody.mytodoquadrantappback.service;

import com.melody.mytodoquadrantappback.model.User;

public interface UserService {
    User getUserById(String id);

    User getUserByEmail(String email);
    User createUser(User user);

}
