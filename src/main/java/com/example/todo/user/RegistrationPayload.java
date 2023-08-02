package com.example.todo.user;

import jakarta.validation.constraints.NotBlank;

public record RegistrationPayload(
        @NotBlank(message = "Missing name")
        String name,

        @NotBlank(message = "Missing email")
        String email,

        @NotBlank(message = "Missing password")
        String password
) {}
