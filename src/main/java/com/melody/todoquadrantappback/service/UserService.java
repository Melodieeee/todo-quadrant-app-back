package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {
    User getUserById(String id);
    User getUserByEmail(String email);
    User getAuthenticatedUser(OAuth2User oAuth2User);
    User createUser(User user);

    void deleteUserById(String id);
    void deleteAllUsers();



}
