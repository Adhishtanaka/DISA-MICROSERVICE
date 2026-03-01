/**
 * Service for publishing resource-related events to RabbitMQ.
 * Handles critical low stock alerts and other resource events.
 */
package com.disa.resource_service.event;

import com.disa.resource_service.entity.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishCriticalLowStock(Resource resource) {
        ResourceEvent event = ResourceEvent.builder()
                .eventType("resource.critical_low")
                .timestamp(LocalDateTime.now())
                .payload(ResourcePayload.builder()
                        .resourceId(resource.getId())
                        .resourceCode(resource.getResourceCode())
                        .name(resource.getName())
                        .currentStock(resource.getCurrentStock())
                        .threshold(resource.getThreshold())
                        .location(resource.getLocation())
                        .build())
                .build();

        rabbitTemplate.convertAndSend("disaster.topic.exchange", "resource.critical_low", event);
        log.info("Published critical low stock event for resource: {}", resource.getResourceCode());
    }
}