package com.example.todo.user;

import jakarta.validation.constraints.NotBlank;

public record LoginPayload(
        @NotBlank(message = "Missing email")
        String email,

        @NotBlank(message = "Missing password")
        String password
) {}
