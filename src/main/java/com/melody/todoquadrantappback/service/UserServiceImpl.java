package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dao.UserDao;
import com.melody.todoquadrantappback.exception.UserNotFoundException;
import com.melody.todoquadrantappback.exception.UserUnauthorizedException;
import com.melody.todoquadrantappback.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(String id) {
        // 這個方法主要給內部使用或測試，不建議 controller 直接調用
        return userDao.findById(id)
                .orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        // 這個方法主要給內部使用或測試，不建議 controller 直接調用
        return userDao.findByEmail(email)
                .orElse(null);
    }

    @Override
    public User getAuthenticatedUser(OAuth2User oAuth2User) {
        // 這個方法給 controller 直接調用
        if (oAuth2User == null) {
            throw new UserUnauthorizedException();
        }

        String email = oAuth2User.getAttribute("email");
        User user = getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }

    @Override
    public User createUser(User user) {
        return userDao.save(user);
    }

    @Override
    public void deleteUserById(String id) {
        userDao.deleteById(id);
    }

    @Override
    public void deleteAllUsers() {
        userDao.deleteAll();
        System.out.println("All users deleted");
    }
}
