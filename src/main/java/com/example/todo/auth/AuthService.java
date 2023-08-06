package com.example.todo.auth;

import com.example.todo.config.JwtUtils;
import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Contains business logic for the auth endpoints.
 */
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Gets a User object from the database for a user with the given email
     * (if a user with that email exists).
     * @param email The email of the user.
     * @return The User data for the user with the given email (if one exists).
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Creates a user in the database with the data from the given payload.
     * @param payload The name, email and password for the new user.
     * @return A User object with the new user's data.
     */
    public User createUser(RegistrationPayload payload) {
        User user = new User();
        user.setName(payload.name().trim());
        user.setEmail(payload.email().trim());
        user.setPassword(passwordEncoder.encode(payload.password().trim()));
        return userRepository.save(user);
    }

    /**
     * Generates a JWT token for a user.
     * @param user The user needing a JWT token.
     * @return A JWT token.
     */
    public String generateUserToken(User user) {
        return jwtUtils.generateToken(user);
    }

    /**
     * Checks if the given raw password is the same as the given encrypted
     * password.
     * @param password The raw password.
     * @param encryptedPassword The encrypted password.
     * @return `true` if the raw password matches the encrypted password.
     * `false` otherwise.
     */
    public boolean isPasswordValid(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
