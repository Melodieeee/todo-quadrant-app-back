package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dao.TaskDao;
import com.melody.todoquadrantappback.dto.CreateTaskRequest;
import com.melody.todoquadrantappback.exception.TaskForbiddenException;
import com.melody.todoquadrantappback.exception.TaskNotFoundException;
import com.melody.todoquadrantappback.model.Task;
import com.melody.todoquadrantappback.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao; // Use interface as variable type to decouple classes

    @Override
    public List<Task> getAllTasks() {
        return taskDao.findAll();
    }
    @Override
    public Task createTaskForUser(CreateTaskRequest request, User user) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCreatedAt(request.getCreatedAt() != null ? request.getCreatedAt() : Instant.now());
        task.setImportant(request.getImportant());
        task.setUrgent(request.getUrgent());
        task.setDueDate(request.getDueDate());
        task.setCompleted(Boolean.TRUE.equals(request.getCompleted()));
        task.setOrderIndex(request.getOrderIndex() != null ? request.getOrderIndex() : 0);
        task.setUserId(user.getId());
        return taskDao.save(task);
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        return taskDao.findByUserId(userId);
    }

    @Override
    public Task getTaskById(String taskId) {
        return taskDao.findById(taskId).orElseThrow(TaskNotFoundException::new);
    }

    @Override
    public Task updateTask(Task task) {
        return taskDao.update(task);
    }

    @Override
    public void deleteTasksByUserId(String userId) {
        taskDao.deleteByUserId(userId);
    }

    @Override
    public void deleteTaskByIdAndCheckOwnership(String taskId, String userId) {
        Task task = getTaskByIdAndCheckOwnership(taskId, userId);
        taskDao.deleteByIdAndUserId(task.getId(), userId);
    }

    @Override
    public void deleteAllTasks() {
        taskDao.deleteAll();
    }

    @Override
    public Task getTaskByIdAndCheckOwnership(String taskId, String userId) {
        Task task = taskDao.findById(taskId).orElseThrow(TaskNotFoundException::new);
        if (!task.getUserId().equals(userId)) {
            throw new TaskForbiddenException();
        }
        return task;
    }
}
