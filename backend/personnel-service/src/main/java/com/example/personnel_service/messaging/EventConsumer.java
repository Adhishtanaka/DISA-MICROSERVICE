package com.example.personnel_service.messaging;

import com.example.personnel_service.config.RabbitMQConfig;
import com.example.personnel_service.event.TaskAssignedEvent;
import com.example.personnel_service.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Consumer for processing incoming RabbitMQ events.
 * 
 * <p>This component listens to RabbitMQ queues and processes events from other
 * microservices. It handles task assignment notifications from the task-service
 * and updates personnel status accordingly.
 * 
 * @author Personnel Service Team
 * @version 1.0
 * @since 2026-02-21
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {
    
    private final PersonService personService;
    
    /**
     * Handles task assignment events from the task-service.
     * 
     * <p>When a task is assigned to personnel by the task-service, this listener
     * receives the event and can update the personnel's status, log the assignment,
     * or trigger notifications.
     * 
     * @param event The task assignment event containing task and personnel details
     */
    @RabbitListener(queues = RabbitMQConfig.TASK_ASSIGNED_QUEUE)
    public void handleTaskAssigned(TaskAssignedEvent event) {
        try {
            log.info("Received task.assigned event: taskId={}, assignedTo={}, priority={}", 
                    event.getPayload().getTaskId(),
                    event.getPayload().getAssignedTo(),
                    event.getPayload().getPriority());
            
            // Extract personnel ID from the assignedTo field (e.g., "PER-301" -> 301)
            String personnelCode = event.getPayload().getAssignedTo();
            
            log.info("Processing task assignment for personnel: {}", personnelCode);
            log.info("Task details - Type: {}, Priority: {}, Location: {}", 
                    event.getPayload().getTaskType(),
                    event.getPayload().getPriority(),
                    event.getPayload().getLocation());
            
            // Here you can add logic to:
            // 1. Update personnel status to "ON_DUTY"
            // 2. Store the task assignment in a local table
            // 3. Send notifications to the personnel
            // 4. Update personnel availability
            
            // Example: Update personnel status (uncomment when ready to use)
            // Long personnelId = extractPersonnelId(personnelCode);
            // personService.updatePersonnelStatusOnTaskAssignment(personnelId, event.getPayload());
            
            log.info("Task assignment processed successfully for personnel: {}", personnelCode);
            
        } catch (Exception e) {
            log.error("Failed to process task.assigned event: {}", e.getMessage(), e);
            // Consider implementing retry logic or dead-letter queue here
        }
    }
    
    /**
     * Extracts numeric personnel ID from personnel code.
     * 
     * @param personnelCode The personnel code (e.g., "PER-301")
     * @return The numeric personnel ID
     */
    private Long extractPersonnelId(String personnelCode) {
        try {
            // Extract the numeric part from "PER-301" -> 301
            String numericPart = personnelCode.replaceAll("[^0-9]", "");
            return Long.parseLong(numericPart);
        } catch (Exception e) {
            log.error("Failed to extract personnel ID from code: {}", personnelCode, e);
            throw new IllegalArgumentException("Invalid personnel code format: " + personnelCode);
        }
    }
}
