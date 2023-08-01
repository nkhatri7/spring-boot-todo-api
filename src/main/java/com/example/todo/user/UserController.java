package com.example.todo.user;

import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new ValidationException("Missing name");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Missing email");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new ValidationException("Missing password");
        }
        if (userService.getUserByEmail(user) != null) {
            throw new ValidationException(
                    "Account with this email already exists"
            );
        }
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser.toDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> signInUser(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new ValidationException("Missing email");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new ValidationException("Missing password");
        }
        User signedInUser = userService.getUserByEmail(user);
        if (signedInUser == null) {
            throw new ValidationException(
                    "Account with this email doesn't exist"
            );
        }
        if (!userService.isPasswordValid(user.getPassword(), signedInUser)) {
            throw new AuthorisationException("Incorrect password");
        }
        return ResponseEntity.ok(signedInUser.toDTO());
    }
}
