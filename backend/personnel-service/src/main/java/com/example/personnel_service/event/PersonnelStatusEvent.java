package com.example.personnel_service.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Event DTO for personnel status change notifications.
 * 
 * <p>This event is published by the personnel-service when personnel status
 * changes (e.g., becomes available, unavailable, or is updated). Other services
 * can consume this event to stay synchronized with personnel availability.
 * 
 * @author Personnel Service Team
 * @version 1.0
 * @since 2026-02-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelStatusEvent {
    
    /**
     * Type of event (e.g., "personnel.status.changed", "personnel.available").
     */
    private String eventType;
    
    /**
     * Timestamp when the event occurred.
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;
    
    /**
     * Event payload containing personnel status details.
     */
    private PersonnelStatusPayload payload;
    
    /**
     * Nested payload class containing personnel status information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PersonnelStatusPayload {
        /**
         * Unique personnel identifier.
         */
        private Long personnelId;
        
        /**
         * Personnel code (e.g., "PER-301").
         */
        private String personnelCode;
        
        /**
         * Full name of the personnel.
         */
        private String fullName;
        
        /**
         * Current status (e.g., "AVAILABLE", "ON_DUTY", "UNAVAILABLE", "OFF_DUTY").
         */
        private String status;
        
        /**
         * Personnel role (e.g., "RESPONDER", "COORDINATOR", "VOLUNTEER").
         */
        private String role;
        
        /**
         * List of skills the personnel has.
         */
        private String skills;
        
        /**
         * Whether the personnel is currently available for assignment.
         */
        private Boolean isAvailable;
        
        /**
         * Additional notes about status change.
         */
        private String notes;
    }
}
