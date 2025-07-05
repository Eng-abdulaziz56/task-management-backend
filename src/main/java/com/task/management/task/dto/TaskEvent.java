package com.task.management.task.dto;

import com.task.management.task.enums.TaskPriority;
import com.task.management.task.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskEvent {
    private String eventType; 
    private Integer taskId;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private String userEmail;
    private String userName;
    private LocalDateTime timestamp;
} 