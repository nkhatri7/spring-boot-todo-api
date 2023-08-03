package com.example.todo.task;

import java.time.LocalDate;

/**
 * A Data Transfer Object for a Task which is used to send task data to the
 * client. It limits the associated user data to just the ID of the user that
 * created the task.
 */
public record TaskDTO(Long Id, Long userId, String title, String description,
        LocalDate dueDate, boolean isComplete) {}
