package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dao.TaskDao;
import com.melody.todoquadrantappback.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    public Task createTask(Task task) {
        return taskDao.save(task);
    }

    @Override
    public List<Task> getTasksByUserId(String userId) {
        return taskDao.findByUserId(userId);
    }

    @Override
    public Task getTaskById(String taskId) {
        return taskDao.findById(taskId);
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
    public void deleteTaskByIdAndUserId(String taskId, String userId) {
        taskDao.deleteByIdAndUserId(taskId, userId);
    }

    @Override
    public void deleteAllTasks() {
        taskDao.deleteAll();
    }
}
