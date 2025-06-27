package com.melody.todoquadrantappback.dao;

import com.melody.todoquadrantappback.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public UserDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user = mongoTemplate.findOne(
            new org.springframework.data.mongodb.core.query.Query(
                org.springframework.data.mongodb.core.query.Criteria.where("email").is(email)
            ),
            User.class
        );
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findById(String id) {
        User user = mongoTemplate.findById(id, User.class);
        return Optional.ofNullable(user);
    }


    @Override
    public User save(User user) {
        return mongoTemplate.save(user);
    }
}
