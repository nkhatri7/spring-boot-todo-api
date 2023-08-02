package com.example.todo.user;

import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.ValidationException;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserDTO> registerUser(
            @RequestBody @Valid RegistrationPayload payload) {
        if (userService.getUserByEmail(payload.email()) != null) {
            throw new ValidationException(
                    "Account with this email already exists"
            );
        }
        User newUser = userService.createUser(payload);
        return new ResponseEntity<>(newUser.toDTO(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> signInUser(
            @RequestBody @Valid LoginPayload payload) {
        User signedInUser = userService.getUserByEmail(payload.email());
        if (signedInUser == null) {
            throw new ValidationException(
                    "Account with this email doesn't exist"
            );
        }
        if (!userService.isPasswordValid(payload.password(), signedInUser)) {
            throw new AuthorisationException("Incorrect password");
        }
        return ResponseEntity.ok(signedInUser.toDTO());
    }
}
