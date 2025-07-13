package com.melody.todoquadrantappback.controller;

import com.melody.todoquadrantappback.dto.CreateTaskRequest;
import com.melody.todoquadrantappback.dto.TaskResponse;
import com.melody.todoquadrantappback.dto.UpdateTaskRequest;
import com.melody.todoquadrantappback.mapper.TaskMapper;
import com.melody.todoquadrantappback.model.Task;
import com.melody.todoquadrantappback.model.User;
import com.melody.todoquadrantappback.service.TaskService;
import com.melody.todoquadrantappback.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public List<String> getAllTasks() {
        //return all tasks - for admin use only
        return taskService.getAllTasks().stream()
                .map(task -> task.getTitle())
                .toList();
    }

    @GetMapping("/user")
    public List<TaskResponse> getAllUserTasks(@AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getAuthenticatedUser(principal);
        logger.info("getAllUserTasks: " + user + " " + taskService.getAllTasks().size() + " task(s) found");
        return taskService.getTasksByUserId(user.getId()).stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request,
                                                   @AuthenticationPrincipal OAuth2User oAuth2User) {
        User user = userService.getAuthenticatedUser(oAuth2User);
        Task task = taskService.createTaskForUser(request, user);
        logger.info("createTask: " + task.getTitle() + " for user: " + user.getEmail());
        return ResponseEntity.ok(TaskMapper.toResponse(task));
    }


    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable String id,
                                           @Valid @RequestBody UpdateTaskRequest request,
                                           @AuthenticationPrincipal OAuth2User oAuth2User) {
        User user = userService.getAuthenticatedUser(oAuth2User);

        Task existing = taskService.getTaskByIdAndCheckOwnership(id, user.getId());

        if (request.getTitle() != null) existing.setTitle(request.getTitle());
        if (request.getDescription() != null) existing.setDescription(request.getDescription());
        if (request.getImportant() != null) existing.setImportant(request.getImportant());
        if (request.getUrgent() != null) existing.setUrgent(request.getUrgent());
        if (request.getDueDate() != null) existing.setDueDate(request.getDueDate());
        if (request.getCompleted() != null) existing.setCompleted(request.getCompleted());
        if (request.getOrderIndex() != null) existing.setOrderIndex(request.getOrderIndex());

        Task updated = taskService.updateTask(existing);
        logger.info("updateTask: " + updated.getTitle());
        return ResponseEntity.ok(TaskMapper.toResponse(updated));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id,
                           @AuthenticationPrincipal OAuth2User principal) {
        User user = userService.getAuthenticatedUser(principal);
        taskService.deleteTaskByIdAndCheckOwnership(id, user.getId());
        logger.info("deleteTask");
        return ResponseEntity.noContent().build();
    }

}
