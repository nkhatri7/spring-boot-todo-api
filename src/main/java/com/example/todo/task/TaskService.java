package com.example.todo.task;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * A service class containing methods for all the business logic for tasks.
 */
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Creates a task for the given user with the data from the given payload.
     * @param payload The data for the new task.
     * @param user The user creating the task.
     * @return A new Task object.
     */
    public Task createTask(NewTaskPayload payload, User user) {
        Task task = new Task();
        task.setTitle(payload.title().trim());
        if (payload.description() == null) {
            task.setDescription("");
        } else {
            task.setDescription(payload.description().trim());
        }
        task.setDueDate(payload.dueDate());
        task.setComplete(false);
        task.setUser(user);
        return taskRepository.save(task);
    }

    /**
     * Gets a Task object for the task that has the given ID (if it exists).
     * @param taskId The ID of a task.
     * @return A Task object (if a task with the ID is found).
     */
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    /**
     * Gets a list of tasks that belong to the user with the given user ID.
     * @param userId The ID of a user.
     * @return A list of tasks.
     */
    public List<Task> getUserTasks(Long userId) {
        return taskRepository.findAllByUserId(userId);
    }

    /**
     * Updates the given task with the updated data from the payload.
     * @param task The task to be updated.
     * @param payload An object containing the new task data.
     * @return A Task object with the updated task data.
     */
    @Transactional
    public Task updateTask(Task task, UpdateTaskPayload payload) {
        if(payload.title() != null
                && !payload.title().trim().isEmpty()
                && !payload.title().trim().equals(task.getTitle())) {
            task.setTitle(payload.title().trim());
        }

        if (payload.description() != null
                && !payload.description().trim().isEmpty()
                && !payload.description().trim()
                        .equals(task.getDescription())) {
            task.setDescription(payload.description().trim());
        }

        if (payload.dueDate() != null
                && !payload.dueDate().equals(task.getDueDate())) {
            task.setDueDate(payload.dueDate());
        }

        if (payload.isComplete() != task.isComplete()) {
            task.setComplete(payload.isComplete());
        }

        return task;
    }

    /**
     * Deletes the task that has the given task ID.
     * @param taskId The ID of a task.
     */
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
