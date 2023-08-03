package com.example.todo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentException(
            MethodArgumentNotValidException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        String message = ex.getFieldError() == null
                ? "Missing field"
                : ex.getFieldError().getDefaultMessage();
        errorResponse.setMessage(message);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CustomErrorResponse> handleValidationException(
            ValidationException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthenticationException(
            AuthenticationException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorisationException.class)
    public ResponseEntity<CustomErrorResponse> handleAuthorisationException(
            AuthorisationException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNotFoundException(
            NotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleException(Exception ex) {
        System.out.println(ex.getClass());
        System.out.println(ex.getMessage());
        CustomErrorResponse errorResponse = new CustomErrorResponse();
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setMessage("Internal Server Error");

        return new ResponseEntity<>(errorResponse,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
