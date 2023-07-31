package com.example.todo.task;

import java.time.LocalDate;

public interface TaskDto {
    Long getId();
    String getTitle();
    String getDescription();
    LocalDate getDueDate();
    boolean isComplete();
    Long getUserId();
}
