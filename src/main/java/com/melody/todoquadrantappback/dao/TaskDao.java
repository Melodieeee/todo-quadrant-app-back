package com.melody.todoquadrantappback.dao;

import com.melody.todoquadrantappback.model.Task;

import java.util.List;

public interface TaskDao {

    Task save(Task task);
    List<Task> findAll();
    List<Task> findByUserId(String userId);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String taskId, String userId);
    void deleteAll();
    Task findById(String id);

    Task update(Task task);
}
