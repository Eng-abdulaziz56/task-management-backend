package com.task.management.task;

import com.task.management.dto.ApiResponse;
import com.task.management.task.dto.CreateTaskRequest;
import com.task.management.task.dto.TaskResponse;
import com.task.management.task.dto.UpdateTaskRequest;
import com.task.management.task.enums.TaskPriority;
import com.task.management.task.enums.TaskStatus;
import com.task.management.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            Authentication authentication
    ) {
        TaskResponse response = taskService.createTask(request, authentication);
        return ResponseUtil.success("Task created successfully", response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks(
            Authentication authentication,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) String search
    ) {
        List<TaskResponse> tasks = taskService.getAllTasks(authentication, priority, status, search);
        return ResponseUtil.success("Tasks retrieved successfully", tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        TaskResponse task = taskService.getTaskById(id, authentication);
        return ResponseUtil.success("Task retrieved successfully", task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateTaskRequest request,
            Authentication authentication
    ) {
        TaskResponse updatedTask = taskService.updateTask(id, request, authentication);
        return ResponseUtil.success("Task updated successfully", updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteTask(
            @PathVariable Integer id,
            Authentication authentication
    ) {
        taskService.deleteTask(id, authentication);
        return ResponseUtil.success("Task deleted successfully", null);
    }
}