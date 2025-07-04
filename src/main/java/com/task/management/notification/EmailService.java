package com.task.management.notification;

import com.task.management.task.dto.TaskEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendTaskNotification(TaskEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.getUserEmail());
            message.setSubject("Task " + event.getEventType() + " Notification");
            message.setText(buildEmailContent(event));
            
            mailSender.send(message);
            log.info("Email notification sent to {} for task {}", event.getUserEmail(), event.getTaskId());
        } catch (Exception e) {
            log.error("Failed to send email notification for task {}: {}", event.getTaskId(), e.getMessage());
        }
    }

    private String buildEmailContent(TaskEvent event) {
        StringBuilder content = new StringBuilder();
        content.append("Hello ").append(event.getUserName()).append(",\n\n");
        
        switch (event.getEventType()) {
            case "CREATED":
                content.append("A new task has been created:\n");
                break;
            case "UPDATED":
                content.append("A task has been updated:\n");
                break;
            case "DELETED":
                content.append("A task has been deleted:\n");
                break;
        }
        
        content.append("\nTask Details:\n");
        content.append("- ID: ").append(event.getTaskId()).append("\n");
        content.append("- Title: ").append(event.getTitle()).append("\n");
        content.append("- Description: ").append(event.getDescription()).append("\n");
        content.append("- Priority: ").append(event.getPriority()).append("\n");
        content.append("- Status: ").append(event.getStatus()).append("\n");
        content.append("- Timestamp: ").append(event.getTimestamp()).append("\n");
        
        content.append("\nBest regards,\nTask Management System");
        
        return content.toString();
    }
} 