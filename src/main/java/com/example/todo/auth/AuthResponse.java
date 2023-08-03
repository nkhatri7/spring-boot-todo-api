package com.example.todo.auth;

public record AuthResponse(Long id, String name, String email,
                           String token) {
}
