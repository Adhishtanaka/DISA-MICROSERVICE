package com.example.personnel_service.messaging;

import com.example.personnel_service.config.RabbitMQConfig;
import com.example.personnel_service.event.PersonnelStatusEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing messages to RabbitMQ.
 * 
 * <p>This service handles publishing personnel-related events to the message broker,
 * enabling asynchronous communication with other microservices (e.g., task-service,
 * notification-service).
 * 
 * @author Personnel Service Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MessagePublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    /**
     * Publishes a personnel status change event.
     * 
     * <p>This method sends status update events when personnel availability changes,
     * allowing other services to react to personnel status in real-time.
     * 
     * @param event The personnel status event to publish
     */
    public void publishPersonnelStatusEvent(PersonnelStatusEvent event) {
        try {
            log.info("Publishing personnel status event: personnelId={}, status={}", 
                    event.getPayload().getPersonnelId(), 
                    event.getPayload().getStatus());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.PERSONNEL_STATUS_KEY,
                event
            );
            
            log.info("Personnel status event published successfully");
        } catch (Exception e) {
            log.error("Failed to publish personnel status event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish event to RabbitMQ", e);
        }
    }
    
    /**
     * Publishes a personnel availability event.
     * 
     * <p>This method is specifically for notifying when personnel becomes available
     * for task assignment, allowing the task-service to consider them for new tasks.
     * 
     * @param event The personnel status event indicating availability
     */
    public void publishPersonnelAvailableEvent(PersonnelStatusEvent event) {
        try {
            log.info("Publishing personnel available event: personnelId={}, personnelCode={}", 
                    event.getPayload().getPersonnelId(), 
                    event.getPayload().getPersonnelCode());
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.PERSONNEL_AVAILABLE_KEY,
                event
            );
            
            log.info("Personnel available event published successfully");
        } catch (Exception e) {
            log.error("Failed to publish personnel available event: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish event to RabbitMQ", e);
        }
    }
    
    /**
     * Generic method to publish any event to a specific routing key.
     * 
     * @param routingKey The routing key for message routing
     * @param event The event object to publish
     */
    public void publishEvent(String routingKey, Object event) {
        try {
            log.info("Publishing event with routing key: {}", routingKey);
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                routingKey,
                event
            );
            
            log.info("Event published successfully to routing key: {}", routingKey);
        } catch (Exception e) {
            log.error("Failed to publish event to routing key {}: {}", routingKey, e.getMessage(), e);
            throw new RuntimeException("Failed to publish event to RabbitMQ", e);
        }
    }
}
