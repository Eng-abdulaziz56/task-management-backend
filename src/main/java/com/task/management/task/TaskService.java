package com.task.management.task;

import com.task.management.config.exception.ResourceNotFoundException;
import com.task.management.task.dto.CreateTaskRequest;
import com.task.management.task.dto.TaskResponse;
import com.task.management.task.dto.UpdateTaskRequest;
import com.task.management.task.enums.TaskPriority;
import com.task.management.task.enums.TaskStatus;
import com.task.management.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskResponse createTask(CreateTaskRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority())
                .status(request.getStatus())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(savedTask);
    }

    public List<TaskResponse> getAllTasks(Authentication authentication, TaskPriority priority, TaskStatus status, String search) {
        User user = (User) authentication.getPrincipal();
        System.out.println("print" + search);
        List<Task> tasks = taskRepository.searchTasks(user, priority, status, search == null ? "" : search);

        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Integer id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        return TaskResponse.fromEntity(task);
    }

    public TaskResponse updateTask(Integer id, UpdateTaskRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }

        if (request.getPriority() != null) {
            task.setPriority(request.getPriority());
        }

        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(updatedTask);
    }

    public void deleteTask(Integer id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Task task = taskRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));

        taskRepository.delete(task);
    }
}