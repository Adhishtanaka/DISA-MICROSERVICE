package com.disa.shelter_service.event;

import com.disa.shelter_service.service.ShelterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = false)
public class EventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EventConsumer.class);

    private final ShelterService shelterService;

    public EventConsumer(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @RabbitListener(queues = "incident.created.queue")
    public void handleIncidentCreated(IncidentEvent event) {
        log.info("Received incident.created event: {}", event);

        try {
            Double latitude = event.getPayload().getLatitude();
            Double longitude = event.getPayload().getLongitude();
            String severity = event.getPayload().getSeverity();

            // Find and prepare nearby shelters
            shelterService.prepareNearbyShelters(latitude, longitude, severity);

            log.info("Successfully prepared shelters for incident: {}",
                    event.getPayload().getIncidentCode());

        } catch (Exception e) {
            log.error("Error processing incident.created event", e);
        }
    }
}
