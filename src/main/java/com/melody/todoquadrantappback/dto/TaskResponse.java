package com.melody.todoquadrantappback.dto;

import java.time.Instant;

public record TaskResponse(String id, String title, String description, Instant createdAt,
                           Instant dueDate, boolean completed, Boolean important,
                           Boolean urgent, int orderIndex) {
}
