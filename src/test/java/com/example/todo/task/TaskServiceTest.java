package com.example.todo.task;

import com.example.todo.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(taskRepository);
    }

    @AfterEach
    void tearDown() {
        reset(taskRepository);
    }

    @Test
    void createTask_itShouldCallTheRepositorySaveMethodWithATaskObject() {
        // Given a NewTaskPayload and a User object
        NewTaskPayload payload = new NewTaskPayload(
                "title",
                "description",
                LocalDate.now()
        );
        User user = new User();
        // When createTask() is called
        taskService.createTask(payload, user);
        // Then TaskRepository's save() method will be called with a Task object
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void createTask_itShouldTrimTheTitleFromThePayload() {
        // Given a NewTaskPayload and a User object
        String title = " title ";
        NewTaskPayload payload = new NewTaskPayload(
                title,
                "description",
                LocalDate.now()
        );
        User user = new User();
        // When createTask() is called
        taskService.createTask(payload, user);
        // Then the task title will be trimmed
        ArgumentCaptor<Task> argumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());
        Task capturedTask = argumentCaptor.getValue();
        assertThat(capturedTask.getTitle()).isNotEqualTo(payload.title());
        assertThat(capturedTask.getTitle()).isEqualTo(payload.title().trim());
    }

    @Test
    void createTask_itShouldTrimTheDescriptionFromThePayload() {
        // Given a NewTaskPayload and a User object
        String description = " description ";
        NewTaskPayload payload = new NewTaskPayload(
                "title",
                description,
                LocalDate.now()
        );
        User user = new User();
        // When createTask() is called
        taskService.createTask(payload, user);
        // Then the task description will be trimmed
        ArgumentCaptor<Task> argumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());
        Task capturedTask = argumentCaptor.getValue();
        assertThat(capturedTask.getDescription())
                .isNotEqualTo(payload.description());
        assertThat(capturedTask.getDescription())
                .isEqualTo(payload.description().trim());
    }

    @Test
    void createTask_itShouldSetTheDescriptionAsAnEmptyStringIfThereIsNoDescriptionFromThePayload() {
        // Given a NewTaskPayload and a User object
        NewTaskPayload payload = new NewTaskPayload(
                "title",
                null,
                LocalDate.now()
        );
        User user = new User();
        // When createTask() is called
        taskService.createTask(payload, user);
        // Then the task description will be trimmed
        ArgumentCaptor<Task> argumentCaptor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(argumentCaptor.capture());
        Task capturedTask = argumentCaptor.getValue();
        assertThat(capturedTask.getDescription()).isEqualTo("");
    }

    @Test
    void getTaskById_itShouldCallTheRepositoryFindByIdMethod() {
        // When getTaskById() is called
        Long id = 1L;
        taskService.getTaskById(id);
        // Then TaskRepository's findById() method is called
        verify(taskRepository).findById(id);
    }

    @Test
    void getTaskById_itShouldCallTheRepositoryFindByIdMethodWithTheIdGiven() {
        // When getTaskById() is called with an ID
        Long id = 1L;
        taskService.getTaskById(id);
        // Then TaskRepository's findById() method is called with the same ID
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(taskRepository).findById(argumentCaptor.capture());
        Long capturedId = argumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void getTaskById_itShouldReturnTheResultFromTheFindByIdMethodFromTheRepository() {
        // Given TaskRepository's findById() method returns this task
        Long id = 1L;
        Task task = new Task("title", "description", LocalDate.now(), false);
        given(taskRepository.findById(id)).willReturn(Optional.of(task));
        // When getTaskById() is called with an ID
        Optional<Task> returnedTask = taskService.getTaskById(id);
        // Then the result of TaskRepository's findById is returned
        assertThat(returnedTask).isEqualTo(Optional.of(task));
    }

    @Test
    void getUserTasks_itShouldCallTheRepositoryFindAllByUserIdMethod() {
        // When getUserTasks() is called
        Long id = 1L;
        taskService.getUserTasks(id);
        // Then TaskRepository's findAllByUserId() method is called
        verify(taskRepository).findAllByUserId(id);
    }

    @Test
    void getUserTasks_itShouldCallTheRepositoryFindAllByUserIdMethodWithTheIdGiven() {
        // When getUserTasks() is called
        Long id = 1L;
        taskService.getUserTasks(id);
        // Then TaskRepository's findAllByUserId() method is called with the
        // same ID
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(taskRepository).findAllByUserId(argumentCaptor.capture());
        Long capturedId = argumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(id);
    }

    @Test
    void getUserTasks_itShouldReturnTheResultFromTheFindAllByUserIdMethodFromTheRepository() {
        // Given TaskRepository's findAllByUserId() method returns this list
        Long id = 1L;
        List<Task> tasks = List.of(
                new Task("task1", "description", LocalDate.now(), false),
                new Task("task2", "description", LocalDate.now(), false)
        );
        given(taskRepository.findAllByUserId(id)).willReturn(tasks);
        // When getUserTasks() is called
        List<Task> returnedTasks = taskService.getUserTasks(id);
        // Then the result of TaskRepository's findAllByUserId() method is
        // returned
        assertThat(returnedTasks).isEqualTo(tasks);
    }

    @Test
    void updateTask_itShouldUpdateTheTitleIfTheTitleIsInThePayloadAndIsDifferentToTheCurrentTitle() {
        // Given a task and a payload with an updated title
        String oldTitle = "old title";
        String updatedTitle = "updated title";
        Task task = new Task(oldTitle, "description", LocalDate.now(), false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                updatedTitle,
                null,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the title of the returned task should be updated
        assertThat(updatedTask.getTitle()).isNotEqualTo(oldTitle);
        assertThat(updatedTask.getTitle()).isEqualTo(payload.title().trim());
    }

    @Test
    void updateTask_itShouldNotUpdateTheTitleIfTheTitleIsInThePayloadAndIsTheSameAsTheCurrentTitle() {
        // Given a task and a payload with a title
        String title = "title";
        Task task = new Task(title, "description", LocalDate.now(), false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                title,
                null,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the title of the returned task should not be updated
        assertThat(updatedTask.getTitle()).isEqualTo(title);
    }

    @Test
    void updateTask_itShouldNotUpdateTheTitleIfTheTitleIsNotInThePayload() {
        // Given a task and a payload with no title
        String title = "title";
        Task task = new Task(title, "description", LocalDate.now(), false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the title of the returned task should not be updated
        assertThat(updatedTask.getTitle()).isEqualTo(title);
    }

    @Test
    void updateTask_itShouldUpdateTheDescriptionIfTheDescriptionIsInThePayloadAndIsDifferentToTheCurrentDescription() {
        // Given a task and a payload with an updated description
        String oldDescription = "old description";
        String updatedDescription = "updated description";
        Task task = new Task("task", oldDescription, LocalDate.now(), false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                updatedDescription,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the description of the returned task should be updated
        assertThat(updatedTask.getDescription()).isNotEqualTo(oldDescription);
        assertThat(updatedTask.getDescription())
                .isEqualTo(payload.description().trim());
    }

    @Test
    void updateTask_itShouldNotUpdateTheDescriptionIfTheDescriptionIsInThePayloadAndIsTheSameASTheCurrentDescription() {
        // Given a task and a payload with the same description
        String description = "description";
        Task task = new Task("task", description, LocalDate.now(), false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                description,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the description of the returned task should not be updated
        assertThat(updatedTask.getDescription()).isEqualTo(description);
    }

    @Test
    void updateTask_itShouldNotUpdateTheDescriptionIfTheDescriptionIsNotInThePayload() {
        // Given a task and a payload with no description
        String description = "description";
        Task task = new Task("task", description, LocalDate.now(), false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the description of the returned task should not be updated
        assertThat(updatedTask.getDescription()).isEqualTo(description);
    }

    @Test
    void updateTask_itShouldUpdateTheDueDateIfTheDueDateIsInThePayloadAndIsDifferentToTheCurrentDueDate() {
        // Given a task and a payload with an updated due date
        LocalDate oldDueDate = LocalDate.of(2023, 8, 7);
        LocalDate updatedDueDate = LocalDate.of(2023, 8, 8);
        Task task = new Task("task", "", oldDueDate, false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                updatedDueDate,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the due date of the returned task should be updated
        assertThat(updatedTask.getDueDate()).isNotEqualTo(oldDueDate);
        assertThat(updatedTask.getDueDate()).isEqualTo(payload.dueDate());
    }

    @Test
    void updateTask_itShouldNotUpdateTheDueDateIfTheDueDateIsInThePayloadAndIsTheSameAsTheCurrentDueDate() {
        // Given a task and a payload with the same due date
        LocalDate dueDate = LocalDate.of(2023, 8, 7);
        Task task = new Task("task", "", dueDate, false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                dueDate,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the due date of the returned task should not be updated
        assertThat(updatedTask.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    void updateTask_itShouldNotUpdateTheDueDateIfTheDueDateIsNotInThePayload() {
        // Given a task and a payload with the same due date
        LocalDate dueDate = LocalDate.of(2023, 8, 7);
        Task task = new Task("task", "", dueDate, false);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                null,
                false
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then the due date of the returned task should not be updated
        assertThat(updatedTask.getDueDate()).isEqualTo(dueDate);
    }

    @Test
    void updateTask_itShouldUpdateIsCompleteIfItIsDifferentToTheCurrentValue() {
        // Given a task and a payload with an updated isComplete
        boolean oldIsComplete = false;
        boolean updatedIsComplete = true;
        LocalDate updatedDueDate = LocalDate.of(2023, 8, 8);
        Task task = new Task("task", "", LocalDate.now(), oldIsComplete);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                null,
                updatedIsComplete
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then isComplete of the returned task should be updated
        assertThat(updatedTask.isComplete()).isNotEqualTo(oldIsComplete);
        assertThat(updatedTask.isComplete()).isEqualTo(payload.isComplete());
    }

    @Test
    void updateTask_itShouldNotUpdateIsCompleteIfItIsTheSameASTheCurrentValue() {
        // Given a task and a payload with an updated isComplete
        boolean isComplete = false;
        LocalDate updatedDueDate = LocalDate.of(2023, 8, 8);
        Task task = new Task("task", "", LocalDate.now(), isComplete);
        UpdateTaskPayload payload = new UpdateTaskPayload(
                null,
                null,
                null,
                isComplete
        );
        // When updateTask() is called with the task and payload
        Task updatedTask = taskService.updateTask(task, payload);
        // Then isComplete of the returned task should not be updated
        assertThat(updatedTask.isComplete()).isEqualTo(isComplete);
    }

    @Test
    void deleteTask_itShouldCallTheRepositoryDeleteByIdMethod() {
        // When deleteTask() is called
        Long id = 1L;
        taskService.deleteTask(id);
        // Then TaskRepository's deleteById() method should be called
        verify(taskRepository).deleteById(id);
    }

    @Test
    void deleteTask_itShouldCallTheRepositoryDeleteByIdMethodWithTheGivenId() {
        // When deleteTask() is called with an ID
        Long id = 1L;
        taskService.deleteTask(id);
        // Then TaskRepository's deleteById() method should be called with the
        // same ID
        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
        verify(taskRepository).deleteById(argumentCaptor.capture());
        Long capturedId = argumentCaptor.getValue();
        assertThat(capturedId).isEqualTo(id);
    }
}