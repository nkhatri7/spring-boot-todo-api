package com.example.todo.task;

import com.example.todo.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @RequestBody NewTaskRequestBody body) {
        if (body.getTitle() == null || body.getTitle().trim().isEmpty()) {
            throw new ValidationException("Missing task title");
        }
        if (body.getDueDate() == null || body.getDueDate().trim().isEmpty()) {
            throw new ValidationException("Missing due date");
        }
        if (body.getUserId() == null) {
            throw new ValidationException("Missing user ID");
        }
        if (body.getDescription() == null) {
            body.setDescription("");
        }
        Task newTask = taskService.createTask(body);
        return new ResponseEntity<>(newTask.toDTO(), HttpStatus.CREATED);
    }
}
