package com.task.management.notification;

import com.task.management.config.RabbitMQConfig;
import com.task.management.task.dto.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskEventConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = RabbitMQConfig.TASK_QUEUE)
    public void handleTaskEvent(TaskEvent event) {
        log.info("Received task event: {} for task ID: {}", event.getEventType(), event.getTaskId());
        
        try {
            emailService.sendTaskNotification(event);
            log.info("Successfully processed task event: {} for task ID: {}", event.getEventType(), event.getTaskId());
        } catch (Exception e) {
            log.error("Error processing task event: {} for task ID: {}", event.getEventType(), event.getTaskId(), e);
        }
    }
} 