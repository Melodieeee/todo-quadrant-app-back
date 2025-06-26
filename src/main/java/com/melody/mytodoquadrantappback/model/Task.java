package com.melody.mytodoquadrantappback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;
    @Indexed
    private String userId;
    private String title;
    private Boolean important;
    private Boolean urgent;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant createdAt;
    private String description;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant dueDate;
    private boolean completed;
    private int orderIndex;

    public Task() {}

    public Task(String id, String userId, String title, Boolean important, Boolean urgent,
                Instant createdAt, String description, Instant dueDate,
                boolean completed, int orderIndex) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.important = important;
        this.urgent = urgent;
        this.createdAt = createdAt;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = completed;
        this.orderIndex = orderIndex;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getTitle() { return title; }
    public Boolean isImportant() { return important; }
    public Boolean isUrgent() { return urgent; }
    public Instant getCreatedAt() { return createdAt; }
    public String getDescription() { return description; }
    public Instant getDueDate() { return dueDate; }
    public boolean isCompleted() { return completed; }
    public int getOrderIndex() { return orderIndex; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setTitle(String title) { this.title = title; }
    public void setImportant(Boolean important) { this.important = important; }
    public void setUrgent(Boolean urgent) { this.urgent = urgent; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public void setOrderIndex(int orderIndex) { this.orderIndex = orderIndex; }
}
