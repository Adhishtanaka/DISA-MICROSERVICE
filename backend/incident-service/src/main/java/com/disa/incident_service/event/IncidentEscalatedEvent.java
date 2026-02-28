/*
 * Incident Escalated Event
 *
 * Event class for incident escalation notifications.
 * Contains escalation-specific payload information.
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
public class IncidentEscalatedEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private EscalationPayload payload;
}