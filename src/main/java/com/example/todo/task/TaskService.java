package com.example.todo.task;

import com.example.todo.exceptions.ValidationException;
import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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

    public Task createTask(NewTaskRequestBody requestBody) {
        User user = userRepository.getUserById(requestBody.getUserId());
        if (user == null) {
            throw new ValidationException("User with ID "
                    + requestBody.getUserId() + " doesn't exist");
        }
        Task task = new Task();
        task.setTitle(requestBody.getTitle());
        task.setDescription(requestBody.getDescription());
        task.setDueDate(LocalDate.parse(requestBody.getDueDate()));
        task.setComplete(false);
        task.setUser(user);
        return taskRepository.save(task);
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.getTaskById(taskId);
    }
}
