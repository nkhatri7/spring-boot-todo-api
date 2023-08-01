package com.example.todo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDto registerNewUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return userService.convertUserToUserDto(newUser);
    }
}
