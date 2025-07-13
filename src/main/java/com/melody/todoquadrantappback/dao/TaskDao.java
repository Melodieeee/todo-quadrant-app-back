package com.melody.todoquadrantappback.dao;

import com.melody.todoquadrantappback.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskDao {

    Task save(Task task);
    List<Task> findAll();
    List<Task> findByUserId(String userId);
    void deleteByUserId(String userId);
    void deleteByIdAndUserId(String taskId, String userId);
    void deleteAll();
    Optional<Task> findById(String id);

    Task update(Task task);
}
