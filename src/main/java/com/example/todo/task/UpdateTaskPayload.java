package com.example.todo.task;

import java.time.LocalDate;

public record UpdateTaskPayload(String title, String description,
                LocalDate dueDate, boolean isComplete) {

}
