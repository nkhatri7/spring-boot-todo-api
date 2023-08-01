package com.example.todo.task;

import com.example.todo.exceptions.NotFoundException;
import com.example.todo.exceptions.ValidationException;
import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Task createTask(TaskDTO taskDTO) {
        User user = userRepository.findById(taskDTO.getUserId())
                .orElseThrow(() -> new ValidationException(
                        "User with ID " + taskDTO.getUserId() + " doesn't exist"
                ));
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        if (taskDTO.getDescription() == null) {
            task.setDescription("");
        } else {
            task.setDescription(taskDTO.getDescription());
        }
        task.setDueDate(taskDTO.getDueDate());
        task.setComplete(false);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() ->
                new NotFoundException("Cannot find task with ID " + taskId)
        );
    }

    public List<Task> getUserTasks(Long userId) {
        return taskRepository.getTasksByUserId(userId);
    }

    @Transactional
    public Task updateTask(Task task, TaskDTO updatedData) {
        if(updatedData.getTitle() != null
                && !updatedData.getTitle().trim().isEmpty()
                && !updatedData.getTitle().trim().equals(task.getTitle())) {
            task.setTitle(updatedData.getTitle());
        }

        if (updatedData.getDescription() != null
                && !updatedData.getDescription().trim().isEmpty()
                && !updatedData.getDescription().trim()
                        .equals(task.getDescription())) {
            task.setDescription(updatedData.getDescription());
        }

        if (updatedData.getDueDate() != null
                && !updatedData.getDueDate().equals(task.getDueDate())) {
            task.setDueDate(updatedData.getDueDate());
        }

        if (updatedData.isComplete() != task.isComplete()) {
            task.setComplete(updatedData.isComplete());
        }

        return task;
    }
}
