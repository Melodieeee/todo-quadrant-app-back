package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dao.UserDao;
import com.melody.todoquadrantappback.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User getUserById(String id) {
        return userDao.findById(id)
                .orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDao.findByEmail(email)
                .orElse(null);
    }

    @Override
    public User createUser(User user) {
        return userDao.save(user);
    }
}
