package com.melody.mytodoquadrantappback.dao;

import com.melody.mytodoquadrantappback.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskDaoImpl implements TaskDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public TaskDaoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Task save(Task task) {
        return mongoTemplate.save(task);
    }

    @Override
    public List<Task> findAll() {
        return mongoTemplate.findAll(Task.class);
    }

    @Override
    public List<Task> findByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Task.class);
    }

    @Override
    public void deleteByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        mongoTemplate.remove(query, Task.class);
    }

    @Override
    public void deleteByIdAndUserId(String taskId, String userId) {
        Query query = new Query(
                Criteria.where("id").is(taskId)
                        .and("userId").is(userId)
        );
        mongoTemplate.remove(query, Task.class);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.dropCollection(Task.class);
    }

    @Override
    public Task findById(String id) {
        return mongoTemplate.findById(id, Task.class);
    }


    @Override
    public Task update(Task task) {
        return mongoTemplate.save(task); // save 會根據 id 自動 insert 或 update
    }
}
