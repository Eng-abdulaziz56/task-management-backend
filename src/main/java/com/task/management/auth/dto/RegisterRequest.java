package com.task.management.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email must be filled")
    @Email(message = "Email must be correct")
    private String email;
    
    @NotBlank(message = "name must be filled")
    @NotBlank
    @NotNull
    private String name;

    @NotBlank(message = "password must be filled")
    @NotNull
    @Size(min = 8, max = 20, message = "Password must be between 8 to 20 characters")
    String password;
}