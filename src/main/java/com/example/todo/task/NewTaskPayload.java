package com.example.todo.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewTaskPayload(
    @NotBlank(message = "Missing task title")
    String title,

    String description,

    @NotNull(message = "Missing due date")
    LocalDate dueDate,

    @NotNull(message = "Missing user ID")
    Long userId
) {}
