package com.example.todo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        String encryptedPassword = encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    public UserDto getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User getUserByEmail(User user) {
        return userRepository.getUserByEmail(user.getEmail());
    }

    public String encryptPassword(String password) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(password, salt);
    }

    public boolean isPasswordValid(String password, User user) {
        return BCrypt.checkpw(password, user.getPassword());
    }
}
