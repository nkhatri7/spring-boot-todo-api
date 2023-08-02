package com.example.todo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(RegistrationPayload payload) {
        String encryptedPassword = encryptPassword(payload.password());
        User user = new User(payload.name(), payload.email(),
                encryptedPassword);
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public String encryptPassword(String password) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(password, salt);
    }

    public boolean isPasswordValid(String password, User user) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}
