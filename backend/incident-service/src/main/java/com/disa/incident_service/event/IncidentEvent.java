/*
 * Incident Event
 *
 * Base event class for incident-related messaging.
 * Contains common event metadata and payload structure.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private Object payload;
}