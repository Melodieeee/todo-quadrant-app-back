package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dto.CreateTaskRequest;
import com.melody.todoquadrantappback.dto.TaskResponse;
import com.melody.todoquadrantappback.model.Task;
import com.melody.todoquadrantappback.model.User;
import org.springframework.http.ResponseEntity;


import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();
    Task createTaskForUser(CreateTaskRequest request, User user);
    List<Task> getTasksByUserId(String userId);
    Task getTaskById(String taskId); // Retrieves a task by its ID but does not check ownership - for admin
    Task updateTask(Task task);
    void deleteTasksByUserId(String userId);
    void deleteTaskByIdAndCheckOwnership(String taskId, String userId);
    void deleteAllTasks();
    Task getTaskByIdAndCheckOwnership(String taskId, String userId);
}
