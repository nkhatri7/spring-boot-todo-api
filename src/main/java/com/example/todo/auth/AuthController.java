package com.example.todo.auth;

import com.example.todo.exceptions.AuthorisationException;
import com.example.todo.exceptions.BadRequestException;
import com.example.todo.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A controller to handle incoming requests for the /api/v1/auth endpoint.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Handles incoming requests for the /api/v1/auth/register endpoint which
     * registers a new user in the system.
     * @param payload The payload for the incoming request.
     * @return The new user data with a JWT token for the user creating an
     * account.
     * @throws BadRequestException If an account with the email from the payload
     * already exists.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @RequestBody @Valid RegistrationPayload payload
    ) {
        if (authService.getUserByEmail(payload.email()).isPresent()) {
            throw new BadRequestException("Account with email already exists");
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

    /**
     * Handles incoming requests for the /api/v1/auth/login endpoint which
     * authenticates the user making the request and sends back the user data
     * with a JWT token.
     * @param payload The payload for the incoming request (email and password).
     * @return The User data and a JWT token for the user making the request.
     * @throws BadRequestException If an account with the given email from the
     * payload doesn't exist.
     * @throws AuthorisationException If the password from the payload is
     * incorrect.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signInUser(
            @RequestBody @Valid LoginPayload payload
    ) {
        User signedInUser = authService.getUserByEmail(payload.email())
                .orElseThrow(() -> new BadRequestException(
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
