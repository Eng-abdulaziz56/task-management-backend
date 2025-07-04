package com.task.management.utils;

import com.task.management.dto.ApiResponse;
import io.micrometer.common.lang.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, @Nullable T data) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiResponse<>("success", message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>("error", message, null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus statusCode) {
        return ResponseEntity
                .status(statusCode)
                .body(new ApiResponse<>("error", message, null));
    }
}