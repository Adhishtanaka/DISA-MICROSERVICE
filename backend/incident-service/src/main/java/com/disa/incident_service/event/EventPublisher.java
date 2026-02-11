/*
 * Event Publisher
 *
 * Service responsible for publishing incident events to RabbitMQ.
 * Handles both incident creation and escalation events.
 */

package com.disa.incident_service.event;

import com.disa.incident_service.entity.Incident;
import com.disa.incident_service.entity.Severity;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishIncidentCreated(Incident incident) {
        IncidentEvent event = new IncidentEvent();
        event.setEventType("incident.created");
        event.setTimestamp(LocalDateTime.now());
        IncidentPayload payload = new IncidentPayload(
            incident.getId(),
            incident.getIncidentCode(),
            incident.getType().name(),
            incident.getSeverity().name(),
            incident.getLatitude(),
            incident.getLongitude(),
            incident.getAddress(),
            incident.getDescription()
        );
        event.setPayload(payload);

        rabbitTemplate.convertAndSend(
            "disaster.topic.exchange",
            "incident.created",
            event
        );
    }

    public void publishIncidentEscalated(Incident incident, Severity previousSeverity) {
        IncidentEscalatedEvent event = new IncidentEscalatedEvent();
        event.setEventType("incident.escalated");
        event.setTimestamp(LocalDateTime.now());
        EscalationPayload payload = new EscalationPayload(
            incident.getId(),
            incident.getIncidentCode(),
            previousSeverity.name(),
            incident.getSeverity().name(),
            "Manual escalation"
        );
        event.setPayload(payload);

        rabbitTemplate.convertAndSend(
            "disaster.topic.exchange",
            "incident.escalated",
            event
        );
    }
}