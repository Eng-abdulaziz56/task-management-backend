package com.task.management.config.exception;

import com.task.management.config.exception.customExceptions.CustomBadRequestException;
import com.task.management.config.exception.customExceptions.DuplicateResourceException;
import com.task.management.dto.ApiResponse;
import com.task.management.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        return (ResponseUtil.error(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        final Optional<String> firstError = errors.values().stream().findFirst();
        return firstError.map(ResponseUtil::error).
                orElseGet(() -> (ResponseUtil.error("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR)));
    }

    // Handles wrong email/password
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseUtil.error("Invalid email or password",  HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateResource(DuplicateResourceException ex) {
        return ResponseUtil.error(ex.getMessage(),  HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleCustomBadRequestException(CustomBadRequestException ex) {
        return ResponseUtil.error(ex.getMessage(),  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseUtil.error(ex.getMessage(),  HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        String errorMessage = "Required request body is missing or malformed";

        if (ex.getMessage() != null && ex.getMessage().contains("Required request body")) {
            errorMessage = "Required request body is missing. Please provide a valid request body.";
        }

        return ResponseUtil.error(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // Optional: Log for debugging in dev environments
        return ResponseUtil.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}