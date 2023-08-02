package com.example.todo.task;

import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
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
            @RequestBody @Valid NewTaskPayload payload) {
        Task newTask = taskService.createTask(payload);
        return new ResponseEntity<>(newTask.toDTO(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable Long id,
               @RequestParam @NonNull Long userId) {
        Task task = taskService.getTaskById(id);
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
              @RequestBody @Valid UpdateTaskPayload payload) {
        Task task = taskService.getTaskById(id);
        User user = task.getUser();
        if (!payload.userId().equals(user.getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        Task updatedTask = taskService.updateTask(task, payload);
        return ResponseEntity.ok(updatedTask.toDTO());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id,
            @RequestParam @NonNull Long userId) {
        Task task = taskService.getTaskById(id);
        User user = task.getUser();
        if (!userId.equals(user.getId())) {
            throw new AuthorisationException("Unauthorised - not your task");
        }
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task was successfully deleted");
    }
}
