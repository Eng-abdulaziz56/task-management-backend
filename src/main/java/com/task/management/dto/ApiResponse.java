package com.task.management.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
}
