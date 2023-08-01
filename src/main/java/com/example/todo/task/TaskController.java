package com.example.todo.task;

import com.example.todo.exceptions.AuthenticationException;
import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.NotFoundException;
import com.example.todo.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id,
                                           @RequestParam Long userId) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            throw new NotFoundException("Cannot find task with ID " + id);
        }
        if (userId == null) {
            throw new AuthenticationException("Unauthenticated");
        }
        if (!userId.equals(task.getUser().getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        return ResponseEntity.ok(task.toDTO());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getUserTasks(
            @PathVariable Long userId) {
        List<TaskDTO> tasks = taskService.getUserTasks(userId).stream()
                .map(Task::toDTO)
                .toList();
        return ResponseEntity.ok(tasks);
    }
}
