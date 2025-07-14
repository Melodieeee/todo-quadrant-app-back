package com.melody.todoquadrantappback.service;

import com.melody.todoquadrantappback.dao.TaskDao;
import com.melody.todoquadrantappback.dto.CreateTaskRequest;
import com.melody.todoquadrantappback.model.Task;
import com.melody.todoquadrantappback.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskDao taskDao;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void getAllTasks_shouldReturnAllTasks() {
        // 1. Arrange
        List<Task> mockTasks = List.of(new Task(), new Task());
        when(taskDao.findAll()).thenReturn(mockTasks);
        //2. Act
        List<Task> result = taskService.getAllTasks();
        //3. Assert
        assertEquals(2, result.size());
        //4. Verify
        verify(taskDao).findAll();
    }

    @Test
    void createTaskForUser_shouldBuildTaskAndSave() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Mock Task");

        User user = new User();
        user.setId("User123");

        Task mockTask = new Task();
        mockTask.setTitle("Mock Task");
        mockTask.setUserId("User123");

        when(taskDao.save(any(Task.class))).thenReturn(mockTask);

        Task result = taskService.createTaskForUser(request, user);

        assertEquals("Mock Task", result.getTitle());
        assertEquals("User123", result.getUserId());
        verify(taskDao).save(any(Task.class));
    }


    @Test
    void getTasksByUserId_shouldReturnUserTasks() {
        String userId = "User123";
        List<Task> mockTasks = List.of(new Task(), new Task());
        when(taskDao.findByUserId(userId)).thenReturn(mockTasks);
        List<Task> result = taskService.getTasksByUserId(userId);
        assertEquals(2, result.size());
        verify(taskDao).findByUserId(userId);
    }

    @Test
    void getTaskById_shouldReturnTask() {
        String taskId = "Task123";
        Task mockTask = new Task();

        when(taskDao.findById(taskId)).thenReturn(Optional.of(mockTask));
        Task result = taskService.getTaskById(taskId);
        assertEquals(mockTask, result);
        verify(taskDao).findById(taskId);
    }

    @Test
    void updateTask_shouldUpdateAndReturnTask() {
        Task updatedTask = new Task();
        updatedTask.setTitle("Updated Task");
        when(taskDao.update(updatedTask)).thenReturn(updatedTask);
        Task result = taskService.updateTask(updatedTask);
        assertEquals("Updated Task", result.getTitle());
        verify(taskDao).update(updatedTask);
    }

    @Test
    void deleteTasksByUserId_shouldCallDao() {
        String userId = "User123";
        taskService.deleteTasksByUserId(userId);
        verify(taskDao).deleteByUserId(userId);
    }

    @Test
    void deleteTaskByIdAndCheckOwnership_shouldDeleteIfUserOwnsTask() {
        String taskId = "task123";
        String userId = "user123";
        Task mockTask = new Task(taskId, userId);

        when(taskDao.findById(taskId)).thenReturn(Optional.of(mockTask));

        taskService.deleteTaskByIdAndCheckOwnership(taskId, userId);

        verify(taskDao).deleteByIdAndUserId(taskId, userId);
    }

    @Test
    void deleteAllTasks_shouldCallDao() {
        taskService.deleteAllTasks();
        verify(taskDao).deleteAll();
    }
}