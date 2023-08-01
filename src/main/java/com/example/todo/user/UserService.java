package com.example.todo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public UserDto getUserById(Long userId) {
        return userRepository.getUserById(userId);
    }

    public User getUserByEmail(User user) {
        return userRepository.getUserByEmail(user.getEmail());
    }
}
