package com.melody.todoquadrantappback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;


public class UpdateTaskRequest {
    @NotBlank(message = "Title cannot be blank")
    @NotNull(message = "Title is required")
    private String title;
    private String description;
    private Boolean important;
    private Boolean urgent;
    private Instant dueDate;
    private Boolean completed;
    private Integer orderIndex;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getImportant() { return important; }
    public void setImportant(Boolean important) { this.important = important; }

    public Boolean getUrgent() { return urgent; }
    public void setUrgent(Boolean urgent) { this.urgent = urgent; }

    public Instant getDueDate() { return dueDate; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
}
