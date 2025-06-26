package com.melody.mytodoquadrantappback.service;

import com.melody.mytodoquadrantappback.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task createTask(Task task);
    List<Task> getTasksByUserId(String userId);
    Task getTaskById(String taskId);
    Task updateTask(Task task);
    void deleteTasksByUserId(String userId);
    void deleteTaskByIdAndUserId(String taskId, String userId);
    void deleteAllTasks();
}
