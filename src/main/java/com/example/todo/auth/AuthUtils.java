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

/**
 * Contains helper/utility methods for authentication and authorisation to be
 * used in controllers to handle incoming requests and validate requests.
 */
@Component
public class AuthUtils {
    private final UserRepository userRepository;

    @Autowired
    public AuthUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets the user ID from the authentication context of the request.
     * @return The user ID of the user making the request.
     * @throws AuthenticationException If the user making the request doesn't
     * exist in the database.
     */
    public Long getUserIdFromAuth() {
        User user = getUserFromAuth().orElseThrow(() -> {
            return new AuthenticationException("Unauthenticated");
        });
        return user.getId();
    }

    /**
     * Gets the user data (if exists) for the user making the request.
     * @return The User object of the user making the request.
     */
    public Optional<User> getUserFromAuth() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        return userRepository.findByEmail(email);
    }
}
