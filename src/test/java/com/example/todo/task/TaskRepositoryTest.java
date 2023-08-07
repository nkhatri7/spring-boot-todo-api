package com.example.todo.task;

import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskRepository taskRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    void findAllByUserId_itShouldReturnAnEmptyListIfNoTasksForAUserExist() {
        // Given there are no tasks assigned to the user with ID 1
        // When userTasks() is called
        List<Task> userTasks = taskRepository.findAllByUserId(1L);
        // Then an empty list is returned
        assertThat(userTasks).isEmpty();
    }

    @Test
    void findAllByUserId_itShouldReturnAListWithOneTaskWhenTheUserHasOneTask() {
        // Given there is 1 task assigned to a user
        User user = new User("test", "test@gmail.com", "password");
        User newUser = userRepository.save(user);
        Task task = new Task("task", "description", LocalDate.now(), false);
        task.setUser(newUser);
        taskRepository.save(task);
        // When userTasks() is called
        List<Task> userTasks = taskRepository.findAllByUserId(newUser.getId());
        // Then a list with 1 task is returned
        assertThat(userTasks.size()).isEqualTo(1);
    }

    @Test
    void findAllByUserId_itShouldReturnAListWithTwoTasksWhenTheUserHasTwoTasks() {
        // Given there are 2 tasks assigned to a user
        User user = new User("test", "test@gmail.com", "password");
        User newUser = userRepository.save(user);
        for (int i = 0; i < 2; i++) {
            String title = "task" + (i + 1);
            Task task = new Task(title, "description", LocalDate.now(), false);
            task.setUser(newUser);
            taskRepository.save(task);
        }
        // When userTasks() is called
        List<Task> userTasks = taskRepository.findAllByUserId(newUser.getId());
        // Then a list with 2 tasks is returned
        assertThat(userTasks.size()).isEqualTo(2);
    }

    @Test
    void findAllByUserId_itShouldReturnAListWithTenTasksWhenTheUserHasTenTasks() {
        // Given there are 10 tasks assigned to a user
        User user = new User("test", "test@gmail.com", "password");
        User newUser = userRepository.save(user);
        for (int i = 0; i < 10; i++) {
            String title = "task" + (i + 1);
            Task task = new Task(title, "description", LocalDate.now(), false);
            task.setUser(newUser);
            taskRepository.save(task);
        }
        // When userTasks() is called
        List<Task> userTasks = taskRepository.findAllByUserId(newUser.getId());
        // Then a list with 10 tasks is returned
        assertThat(userTasks.size()).isEqualTo(10);
    }
}