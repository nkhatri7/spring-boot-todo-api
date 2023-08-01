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

    public UserDto convertUserToUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }
}
