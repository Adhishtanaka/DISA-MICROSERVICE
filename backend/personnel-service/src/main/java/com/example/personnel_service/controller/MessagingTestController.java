package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.event.PersonnelStatusEvent;
import com.example.personnel_service.messaging.MessagePublisher;
import com.example.personnel_service.service.PersonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST controller for testing RabbitMQ message publishing.
 * 
 * <p>This controller provides endpoints to manually publish test events to RabbitMQ
 * for testing and debugging purposes. In production, these endpoints should be secured
 * or removed.
 * 
 * @author Personnel Service Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/messaging")
@RequiredArgsConstructor
@Slf4j
public class MessagingTestController {
    
    private final MessagePublisher messagePublisher;
    private final PersonService personService;
    
    /**
     * Publishes a test personnel status event.
     * 
     * @param personnelId The ID of the personnel to publish event for
     * @return ResponseEntity with success message
     */
    @PostMapping("/test/publish/status/{personnelId}")
    public ResponseEntity<String> publishTestStatusEvent(@PathVariable Long personnelId) {
        try {
            Person person = personService.getPersonById(personnelId);
            
            PersonnelStatusEvent event = new PersonnelStatusEvent();
            event.setEventType("personnel.status.changed");
            event.setTimestamp(LocalDateTime.now());
            
            PersonnelStatusEvent.PersonnelStatusPayload payload = new PersonnelStatusEvent.PersonnelStatusPayload();
            payload.setPersonnelId(person.getId());
            payload.setPersonnelCode("PER-" + person.getId());
            payload.setFullName(person.getFirstName() + " " + person.getLastName());
            payload.setStatus(person.isDisabled() ? "UNAVAILABLE" : "AVAILABLE");
            payload.setRole(person.getRole() != null ? person.getRole() : "UNKNOWN");
            payload.setIsAvailable(!person.isDisabled());
            payload.setNotes("Test event triggered manually");
            
            event.setPayload(payload);
            
            messagePublisher.publishPersonnelStatusEvent(event);
            
            log.info("Test status event published for personnel ID: {}", personnelId);
            return ResponseEntity.ok("Test status event published successfully for personnel ID: " + personnelId);
            
        } catch (Exception e) {
            log.error("Failed to publish test status event: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Failed to publish test event: " + e.getMessage());
        }
    }
    
    /**
     * Publishes a test personnel available event.
     * 
     * @param personnelId The ID of the personnel to publish event for
     * @return ResponseEntity with success message
     */
    @PostMapping("/test/publish/available/{personnelId}")
    public ResponseEntity<String> publishTestAvailableEvent(@PathVariable Long personnelId) {
        try {
            Person person = personService.getPersonById(personnelId);
            
            PersonnelStatusEvent event = new PersonnelStatusEvent();
            event.setEventType("personnel.available");
            event.setTimestamp(LocalDateTime.now());
            
            PersonnelStatusEvent.PersonnelStatusPayload payload = new PersonnelStatusEvent.PersonnelStatusPayload();
            payload.setPersonnelId(person.getId());
            payload.setPersonnelCode("PER-" + person.getId());
            payload.setFullName(person.getFirstName() + " " + person.getLastName());
            payload.setStatus("AVAILABLE");
            payload.setRole(person.getRole() != null ? person.getRole() : "UNKNOWN");
            payload.setIsAvailable(true);
            payload.setNotes("Test availability event triggered manually");
            
            event.setPayload(payload);
            
            messagePublisher.publishPersonnelAvailableEvent(event);
            
            log.info("Test available event published for personnel ID: {}", personnelId);
            return ResponseEntity.ok("Test available event published successfully for personnel ID: " + personnelId);
            
        } catch (Exception e) {
            log.error("Failed to publish test available event: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Failed to publish test event: " + e.getMessage());
        }
    }
    
    /**
     * Publishes a custom test event with specified routing key.
     * 
     * @param routingKey The routing key to use
     * @param eventType The event type
     * @param personnelId The personnel ID
     * @return ResponseEntity with success message
     */
    @PostMapping("/test/publish/custom")
    public ResponseEntity<String> publishCustomEvent(
            @RequestParam String routingKey,
            @RequestParam String eventType,
            @RequestParam Long personnelId) {
        try {
            Person person = personService.getPersonById(personnelId);
            
            PersonnelStatusEvent event = new PersonnelStatusEvent();
            event.setEventType(eventType);
            event.setTimestamp(LocalDateTime.now());
            
            PersonnelStatusEvent.PersonnelStatusPayload payload = new PersonnelStatusEvent.PersonnelStatusPayload();
            payload.setPersonnelId(person.getId());
            payload.setPersonnelCode("PER-" + person.getId());
            payload.setFullName(person.getFirstName() + " " + person.getLastName());
            payload.setStatus(person.isDisabled() ? "UNAVAILABLE" : "AVAILABLE");
            payload.setRole(person.getRole() != null ? person.getRole() : "UNKNOWN");
            payload.setIsAvailable(!person.isDisabled());
            payload.setNotes("Custom test event: " + eventType);
            
            event.setPayload(payload);
            
            messagePublisher.publishEvent(routingKey, event);
            
            log.info("Custom test event published: routingKey={}, eventType={}, personnelId={}", 
                    routingKey, eventType, personnelId);
            return ResponseEntity.ok(String.format(
                    "Custom event published successfully: routingKey=%s, eventType=%s, personnelId=%d",
                    routingKey, eventType, personnelId));
            
        } catch (Exception e) {
            log.error("Failed to publish custom test event: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Failed to publish custom event: " + e.getMessage());
        }
    }
    
    /**
     * Health check endpoint for messaging service.
     * 
     * @return ResponseEntity with health status
     */
    @GetMapping("/health")
    public ResponseEntity<String> checkMessagingHealth() {
        try {
            log.info("Messaging health check requested");
            return ResponseEntity.ok("Messaging service is healthy");
        } catch (Exception e) {
            log.error("Messaging health check failed: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body("Messaging service is unhealthy: " + e.getMessage());
        }
    }
}
