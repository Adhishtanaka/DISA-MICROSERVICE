package com.disa.logistic_service.event;

import com.disa.logistic_service.service.MissionService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * Consumes events from the message broker to trigger automatic mission creation.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class EventConsumer {

    private final MissionService missionService;

    /**
     * Listens for new incidents and creates a standard delivery mission.
     */
    @RabbitListener(queues = "incident.created.queue")
    public void handleIncidentCreated(IncidentEvent event) {
        log.info("Received incident.created event: {}", event);
        try {
            missionService.createMissionForIncident(
                    event.getPayload().getIncidentId(),
                    event.getPayload().getAddress(),
                    event.getPayload().getSeverity()
            );
        } catch (Exception e) {
            log.error("Error processing incident.created event", e);
        }
    }

    /**
     * Listens for incident escalations and creates an urgent rescue mission.
     */
    @RabbitListener(queues = "incident.escalated.queue")
    public void handleIncidentEscalated(IncidentEscalatedEvent event) {
        log.info("Received incident.escalated event: {}", event);
        try {
            missionService.createUrgentRescueMission(
                    event.getPayload().getIncidentId(),
                    event.getPayload().getNewSeverity()
            );
        } catch (Exception e) {
            log.error("Error processing incident.escalated event", e);
        }
    }

    /**
     * Listens for low resource alerts and creates a restocking mission.
     */
    @RabbitListener(queues = "resource.critical_low.queue")
    public void handleResourceCriticalLow(ResourceEvent event) {
        log.info("Received resource.critical_low event: {}", event);
        try {
            missionService.createResourceDeliveryMission(
                    event.getPayload().getResourceId(),
                    event.getPayload().getResourceType(),
                    event.getPayload().getLocation()
            );
        } catch (Exception e) {
            log.error("Error processing resource.critical_low event", e);
        }
    }

    // --- Inner DTO Classes for Events (to keep file count manageable) ---

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class IncidentEvent {
        private String eventType;
        private LocalDateTime timestamp;
        private IncidentPayload payload;
    }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    static class IncidentPayload {
        private Long incidentId;
        private String incidentCode;
        private String severity;
        private String address;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class IncidentEscalatedEvent {
        private String eventType;
        private EscalationPayload payload;
    }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    static class EscalationPayload {
        private Long incidentId;
        private String incidentCode;
        private String newSeverity;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ResourceEvent {
        private String eventType;
        private ResourcePayload payload;
    }
    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    static class ResourcePayload {
        private Long resourceId;
        private String resourceCode;
        private String resourceType;
        private String location;
    }
}