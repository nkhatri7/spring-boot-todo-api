package com.example.todo.exceptions;

public class AuthorisationException extends RuntimeException {
    public AuthorisationException(String message) {
        super(message);
    }
}
