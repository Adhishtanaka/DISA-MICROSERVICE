/*
 * Incident Event
 *
 * Base event class for incident-related events.
 * Contains common fields like event type, timestamp, and payload.
 */

package com.disa.incident_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private Object payload;
}