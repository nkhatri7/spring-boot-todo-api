package com.example.todo.task;

import com.example.todo.auth.AuthUtils;
import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.NotFoundException;
import com.example.todo.exceptions.BadRequestException;
import com.example.todo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller to handle incoming requests for the /api/v1/task endpoints.
 */
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

    /**
     * Handles incoming POST requests for the /api/v1/task endpoint by creating
     * a task with the given payload in the request body.
     * @param payload The payload from the request body.
     * @return An object with data for the created task.
     * @throws BadRequestException If the user making the request doesn't exist
     * in the database.
     */
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(
            @RequestBody @Valid NewTaskPayload payload) {
        User user = authUtils.getUserFromAuth().orElseThrow(() -> {
            return new BadRequestException("Invalid user");
        });
        Task newTask = taskService.createTask(payload, user);
        return new ResponseEntity<>(newTask.toDTO(), HttpStatus.CREATED);
    }

    /**
     * Handles incoming GET requests for the /api/v1/task/{id} endpoint by
     * getting the data for task with the given ID from the endpoint path
     * variable.
     * @param id The ID from the endpoint.
     * @return An object with data for the task with that ID.
     * @throws NotFoundException If there is no task with that ID.
     * @throws AuthorisationException If the task with that ID doesn't belong
     * to the user making the request.
     */
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

    /**
     * Handles incoming GET requests for the /api/v1/task/user/{userId} endpoint
     * by getting all the tasks that belong to the user with that user ID.
     * @param userId The user ID from the endpoint.
     * @return A list of all the tasks that belong to the user with that ID.
     * @throws AuthorisationException If the user ID of the user making the
     * request is not the same as the user ID from the endpoint.
     */
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

    /**
     * Handles incoming PUT requests for the /api/v1/task/{id} endpoint by
     * updating the task with that ID.
     * @param id The ID from the endpoint.
     * @param payload The payload from the request body.
     * @return An object of the updated task with that ID.
     * @throws NotFoundException If there is no task with that ID.
     * @throws AuthorisationException If the task with that ID doesn't belong to
     * the user making the request.
     */
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

    /**
     * Handles incoming DELETE requests for the /api/v1/task/{id} endpoint by
     * deleting the task with that ID
     * @param id The ID from the endpoint.
     * @return A success message if the task was successfully deleted.
     * @throws NotFoundException If there is no task with that ID.
     * @throws AuthorisationException If the task with that ID doesn't belong to
     * the user making the request.
     */
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
