package com.example.todo.task;

import com.example.todo.exceptions.AuthenticationException;
import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.ValidationException;
import com.example.todo.user.User;
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
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO body) {
        if (body.getTitle() == null || body.getTitle().trim().isEmpty()) {
            throw new ValidationException("Missing task title");
        }
        if (body.getDueDate() == null) {
            throw new ValidationException("Missing due date");
        }
        if (body.getUserId() == null) {
            throw new ValidationException("Missing user ID");
        }
        Task newTask = taskService.createTask(body);
        return new ResponseEntity<>(newTask.toDTO(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id,
               @RequestParam Long userId) {
        Task task = taskService.getTaskById(id);
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

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,
              @RequestBody TaskDTO body) {
        Task task = taskService.getTaskById(id);
        if (body.getUserId() == null) {
            throw new ValidationException("Missing user ID");
        }
        User user = task.getUser();
        if (!body.getUserId().equals(user.getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        Task updatedTask = taskService.updateTask(task, body);
        return ResponseEntity.ok(updatedTask.toDTO());
    }
}
