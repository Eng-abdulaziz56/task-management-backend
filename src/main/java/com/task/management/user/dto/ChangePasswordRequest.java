package com.task.management.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    @NotNull(message = "Old password cannot be null")
    private String oldPassword;

    @NotNull(message = "New password cannot be null")
    @Size(min = 8, max = 20, message = "Password must be between 8 to 20 characters")
    String newPassword;
}