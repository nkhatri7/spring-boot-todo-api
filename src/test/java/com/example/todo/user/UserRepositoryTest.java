package com.example.todo.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void findByEmail_itShouldReturnUserIfUserWithEmailExists() {
        // Given a user with the email test@gmail.com exists
        String email = "test@gmail.com";
        User user = new User("test", email, "password");
        userRepository.save(user);
        // When findByEmail is called
        Optional<User> userInDb = userRepository.findByEmail(user.getEmail());
        // Then result is a User object
        assertThat(userInDb).isNotEmpty();
    }

    @Test
    void findByEmail_itShouldReturnAnEmptyObjectIfUserWithEmailDoesNotExist() {
        // Given a user with the email test@gmail.com doesn't exist
        String email = "test@gmail.com";
        // When findByEmail is called
        Optional<User> userInDb = userRepository.findByEmail(email);
        // Then the result is empty
        assertThat(userInDb).isEmpty();
    }
}