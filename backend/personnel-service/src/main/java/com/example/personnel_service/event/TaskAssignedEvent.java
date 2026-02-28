package com.example.personnel_service.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event DTO for task assignment notifications from task-service.
 * 
 * <p>This event is published by the task-service when a task is assigned to
 * personnel. The personnel-service consumes this event to update personnel
 * status and track active assignments.
 * 
 * @author Personnel Service Team
 * @version 1.0
 * @since 2026-02-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignedEvent {
    
    /**
     * Type of event (e.g., "task.assigned").
     */
    private String eventType;
    
    /**
     * Timestamp when the event occurred.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
    
    /**
     * Event payload containing task assignment details.
     */
    private TaskAssignmentPayload payload;
    
    /**
     * Nested payload class containing task assignment details.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TaskAssignmentPayload {
        /**
         * Unique task identifier (e.g., "TSK-401").
         */
        private String taskId;
        
        /**
         * Personnel ID assigned to the task (e.g., "PER-301").
         */
        private String assignedTo;
        
        /**
         * Type of task (e.g., "RESCUE_OPERATION", "MEDICAL_AID").
         */
        private String taskType;
        
        /**
         * Priority level (e.g., "HIGH", "URGENT", "MEDIUM", "LOW").
         */
        private String priority;
        
        /**
         * Location where task needs to be performed.
         */
        private String location;
        
        /**
         * Detailed description of the task.
         */
        private String description;
    }
}
