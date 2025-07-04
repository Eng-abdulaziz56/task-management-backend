package com.task.management.task.dto;

import com.task.management.task.enums.TaskPriority;
import com.task.management.task.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskRequest {

    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    private String description;
    
    @NotNull(message = "Priority cannot be null")
    private TaskPriority priority;
    
    @NotNull(message = "Status cannot be null")
    private TaskStatus status;
}