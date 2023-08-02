package com.example.todo.task;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Optional<User> getTaskUser(Long userId) {
        return userRepository.findById(userId);
    }

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

    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.getTasksByUserId(userId);
    }

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

    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
