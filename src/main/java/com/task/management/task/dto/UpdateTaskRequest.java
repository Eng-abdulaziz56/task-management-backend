package com.task.management.task.dto;

import com.task.management.task.enums.TaskPriority;
import com.task.management.task.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaskRequest {

    private String title;
    
    private String description;
    
    private TaskPriority priority;
    
    private TaskStatus status;
}