package com.example.todo.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A Repository for tasks to handle database interactions with the task table.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.user.id = ?1")
    List<Task> findAllByUserId(Long userId);
}
