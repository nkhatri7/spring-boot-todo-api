package com.example.todo.auth;

import com.example.todo.exceptions.AuthenticationException;
import com.example.todo.user.User;
import com.example.todo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthUtils {
    private final UserRepository userRepository;

    @Autowired
    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getUserIdFromAuth() {
        User user = getUserFromAuth().orElseThrow(() -> {
            return new AuthenticationException("Unauthenticated");
        });
        return user.getId();
    }

    public Optional<User> getUserFromAuth() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email);
    }
}
