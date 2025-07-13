package com.melody.todoquadrantappback.mapper;

import com.melody.todoquadrantappback.dto.CreateTaskRequest;
import com.melody.todoquadrantappback.dto.TaskResponse;
import com.melody.todoquadrantappback.model.Task;

public class TaskMapper {

    // This method maps a Task object to a TaskResponse DTO.
    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreatedAt(),
                task.getDueDate(),
                task.isCompleted(),
                task.isImportant(),
                task.isUrgent(),
                task.getOrderIndex()
        );
    }

    public static Task toEntity(CreateTaskRequest req, String userId) {
        Task task = new Task();
        task.setTitle(req.getTitle());
        task.setUserId(userId); // 從登入者資訊補進來
        task.setDescription(req.getDescription());
        task.setCreatedAt(req.getCreatedAt());
        task.setDueDate(req.getDueDate());
        task.setCompleted(req.getCompleted());
        task.setImportant(req.getImportant());
        task.setUrgent(req.getUrgent());
        task.setOrderIndex(req.getOrderIndex());
        return task;
    }
}
