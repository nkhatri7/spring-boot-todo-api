package com.example.todo.auth;

import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.ValidationException;
import com.example.todo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @RequestBody @Valid RegistrationPayload payload
    ) {
        if (authService.getUserByEmail(payload.email()).isPresent()) {
            throw new ValidationException("Account with email already exists");
        }
        User newUser = authService.createUser(payload);
        String token = authService.generateUserToken(newUser);
        AuthResponse response = new AuthResponse(
                newUser.getId(),
                newUser.getName(),
                newUser.getEmail(),
                token
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signInUser(
            @RequestBody @Valid LoginPayload payload
    ) {
        User signedInUser = authService.getUserByEmail(payload.email())
                .orElseThrow(() -> new ValidationException(
                        "Account with this email doesn't exist"
                ));
        if (!authService.isPasswordValid(payload.password(),
                signedInUser.getPassword())) {
            throw new AuthorisationException("Incorrect password");
        }
        String token = authService.generateUserToken(signedInUser);
        return ResponseEntity.ok(new AuthResponse(
                signedInUser.getId(),
                signedInUser.getName(),
                signedInUser.getEmail(),
                token
        ));
    }
}
