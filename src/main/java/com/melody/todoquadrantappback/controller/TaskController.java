package com.melody.todoquadrantappback.controller;

import com.melody.todoquadrantappback.model.Task;
import com.melody.todoquadrantappback.model.User;
import com.melody.todoquadrantappback.service.TaskService;
import com.melody.todoquadrantappback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;


    @GetMapping("/all")
    public List<String> getAllTasks() {
        //return all tasks
        return taskService.getAllTasks().stream()
                .map(task -> task.getTitle())
                .toList();
    }

    @GetMapping("/user")
    public List<Task> getAllUserTasks(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return List.of();
        String email = principal.getAttribute("email");
        User user = userService.getUserByEmail(email);
        System.out.println("getAllUserTasks: " + user + " " + taskService.getAllTasks().size() + " tasks found");
        return taskService.getTasksByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Map<String, Object> body,
                                           @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = oAuth2User.getAttribute("email");
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Task task = new Task();
        task.setTitle((String) body.get("title"));
        task.setDescription((String) body.get("description"));
        task.setCreatedAt(parseDateOrNow((String) body.get("createdAt")));
        task.setImportant((Boolean) body.getOrDefault("important", null));
        task.setUrgent((Boolean) body.getOrDefault("urgent", null));
        task.setDueDate(parseDate((String) body.get("dueDate")));
        task.setCompleted((Boolean) body.getOrDefault("completed", false));
        task.setOrderIndex((Integer) body.getOrDefault("orderIndex", 0));
        task.setUserId(user.getId());

        Task saved = taskService.createTask(task);
        System.out.println("createTask: " + saved.getTitle());
        return ResponseEntity.ok(saved);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable String id,
                                           @RequestBody Map<String, Object> body,
                                           @AuthenticationPrincipal OAuth2User oAuth2User) {
        if (oAuth2User == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = oAuth2User.getAttribute("email");
        User user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Task existing = taskService.getTaskById(id);
        if (existing == null || !existing.getUserId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        existing.setTitle((String) body.get("title"));
        existing.setDescription((String) body.get("description"));
        existing.setImportant((Boolean) body.getOrDefault("important", false));
        existing.setUrgent((Boolean) body.getOrDefault("urgent", false));
        existing.setDueDate(parseDate((String) body.get("dueDate")));
        existing.setCompleted((Boolean) body.getOrDefault("completed", false));
        existing.setOrderIndex((Integer) body.getOrDefault("orderIndex", 0));

        Task updated = taskService.updateTask(existing);
        System.out.println("updateTask: " + updated.getTitle());
        return ResponseEntity.ok(updated);
    }



    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable String id,
                           @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return;
        String email = principal.getAttribute("email");
        User user = userService.getUserByEmail(email);
        taskService.deleteTaskByIdAndUserId(id, user.getId());
        System.out.println("deleteTask");
    }

    private Instant parseDate(String input) {
        try {
            return (input != null) ? Instant.parse(input) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Instant parseDateOrNow(String input) {
        Instant parsed = parseDate(input);
        return parsed != null ? parsed : Instant.now();
    }


}
