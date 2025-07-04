package com.task.management.task.service;

import com.task.management.config.RabbitMQConfig;
import com.task.management.task.Task;
import com.task.management.task.dto.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void publishTaskCreated(Task task) {
        TaskEvent event = TaskEvent.builder()
                .eventType("CREATED")
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .userEmail(task.getUser().getEmail())
                .userName(task.getUser().getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.TASK_EXCHANGE, "task.created", event);
        log.info("Task created event published: {}", event.getTaskId());
    }

    public void publishTaskUpdated(Task task) {
        TaskEvent event = TaskEvent.builder()
                .eventType("UPDATED")
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .userEmail(task.getUser().getEmail())
                .userName(task.getUser().getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.TASK_EXCHANGE, "task.updated", event);
        log.info("Task updated event published: {}", event.getTaskId());
    }

    public void publishTaskDeleted(Task task) {
        TaskEvent event = TaskEvent.builder()
                .eventType("DELETED")
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority())
                .status(task.getStatus())
                .userEmail(task.getUser().getEmail())
                .userName(task.getUser().getUsername())
                .timestamp(LocalDateTime.now())
                .build();

        rabbitTemplate.convertAndSend(RabbitMQConfig.TASK_EXCHANGE, "task.deleted", event);
        log.info("Task deleted event published: {}", event.getTaskId());
    }
} 