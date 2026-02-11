/*
 * Incident Escalated Event
 *
 * Event class for incident escalation events.
 * Published when an incident's severity is increased.
 */

package com.disa.incident_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentEscalatedEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private EscalationPayload payload;
}