package com.task.management.auth.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Password cannot be null")
    String password;


    @AssertTrue(message = "first number should be less than second")
    private boolean isFirstLessThanSecond() {
        if (first == null || second == null) return true;
        return first < second;
    }

    private Integer first;
    private Integer second;
}