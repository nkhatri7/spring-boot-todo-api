package com.example.todo.task;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateTaskPayload(
        @NotNull(message = "Missing user ID")
        Long userId,
        String title,
        String description,
        LocalDate dueDate,
        boolean isComplete
) {}
