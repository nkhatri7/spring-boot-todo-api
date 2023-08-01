package com.example.todo.task;

import com.example.todo.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity(name = "Task")
@Table(name = "task")
public class Task {
    @Id
    @SequenceGenerator(
            name = "task_sequence",
            sequenceName = "task_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "task_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String title;
    private String description;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Column(nullable = false)
    private boolean isComplete;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Task() {}

    public Task(String title, String description, LocalDate dueDate,
            boolean isComplete) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.isComplete = isComplete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean completed) {
        this.isComplete = completed;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TaskDTO toDTO() {
        return new TaskDTO(this.id, this.user.getId(), this.title,
                this.description, this.dueDate, this.isComplete);
    }
}
