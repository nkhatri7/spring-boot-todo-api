package com.example.todo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.id, u.name, u.email FROM User u WHERE u.id = ?1")
    UserDto getUserById(Long userId);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User getUserByEmail(String email);
}
