package com.example.todo.auth;

import com.example.todo.config.JwtService;
import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthService(UserRepository userRepository,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(RegistrationPayload payload) {
        User user = new User(payload.name().trim(), payload.email().trim(),
                passwordEncoder.encode(payload.password().trim()));
        return userRepository.save(user);
    }

    public String generateUserToken(User user) {
        return jwtService.generateToken(user);
    }

    public boolean isPasswordValid(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
