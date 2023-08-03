package com.example.todo.task;

import com.example.todo.auth.AuthUtils;
import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.NotFoundException;
import com.example.todo.exceptions.ValidationException;
import com.example.todo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {
    private final TaskService taskService;
    private final AuthUtils authUtils;

    @Autowired
    public TaskController(TaskService taskService, AuthUtils authUtils) {
        this.taskService = taskService;
        this.authUtils = authUtils;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @RequestBody @Valid NewTaskPayload payload) {
        User user = authUtils.getUserFromAuth().orElseThrow(() -> {
            return new ValidationException("Invalid user");
        });
        Task newTask = taskService.createTask(payload, user);
        return new ResponseEntity<>(newTask.toDTO(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id).orElseThrow(() -> {
            return new NotFoundException("Cannot find task with ID " + id);
        });
        Long userId = authUtils.getUserIdFromAuth();
        if (!userId.equals(task.getUser().getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        return ResponseEntity.ok(task.toDTO());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getUserTasks(
            @PathVariable Long userId) {
        Long authUserId = authUtils.getUserIdFromAuth();
        if (!authUserId.equals(userId)) {
            throw new AuthorisationException("Unauthorised");
        }
        List<TaskDTO> tasks = taskService.getUserTasks(userId).stream()
                .map(Task::toDTO)
                .toList();
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id,
              @RequestBody @Valid UpdateTaskPayload payload) {
        Task task = taskService.getTaskById(id).orElseThrow(() -> {
            return new NotFoundException("Cannot find task with ID " + id);
        });
        Long userId = authUtils.getUserIdFromAuth();
        if (!userId.equals(task.getUser().getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        Task updatedTask = taskService.updateTask(task, payload);
        return ResponseEntity.ok(updatedTask.toDTO());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id).orElseThrow(() -> {
            return new NotFoundException("Cannot find task with ID " + id);
        });
        Long userId = authUtils.getUserIdFromAuth();
        if (!userId.equals(task.getUser().getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task was successfully deleted");
    }
}
