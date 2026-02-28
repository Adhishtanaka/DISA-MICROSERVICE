/*
 * Event Publisher
 *
 * Publishes incident-related events to RabbitMQ message broker.
 * Handles incident creation and escalation event publishing.
 *
 * @author Generated
 * @version 1.0
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
        IncidentEvent event = IncidentEvent.builder()
            .eventType("incident.created")
            .timestamp(LocalDateTime.now())
            .payload(IncidentPayload.builder()
                .incidentId(incident.getId())
                .incidentCode(incident.getIncidentCode())
                .type(incident.getType().name())
                .severity(incident.getSeverity().name())
                .latitude(incident.getLatitude())
                .longitude(incident.getLongitude())
                .address(incident.getAddress())
                .description(incident.getDescription())
                .build())
            .build();

        rabbitTemplate.convertAndSend(
            "disaster.topic.exchange",
            "incident.created",
            event
        );
    }

    public void publishIncidentEscalated(Incident incident, Severity previousSeverity) {
        IncidentEscalatedEvent event = IncidentEscalatedEvent.builder()
            .eventType("incident.escalated")
            .timestamp(LocalDateTime.now())
            .payload(EscalationPayload.builder()
                .incidentId(incident.getId())
                .incidentCode(incident.getIncidentCode())
                .previousSeverity(previousSeverity.name())
                .newSeverity(incident.getSeverity().name())
                .reason("Manual escalation")
                .build())
            .build();

        rabbitTemplate.convertAndSend(
            "disaster.topic.exchange",
            "incident.escalated",
            event
        );
    }
}