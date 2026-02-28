/**
 * IncidentEvent.java
 *
 * Represents a domain event message received from the RabbitMQ message broker,
 * published by the Incident Service when a new incident is created.
 *
 * This event is consumed by the Shelter Service's EventConsumer to trigger
 * shelter preparation workflows in response to nearby incidents.
 *
 * Fields:
 *   - eventType : Type identifier of the event (e.g., "incident.created")
 *   - timestamp : Date and time when the event was published
 *   - payload   : Detailed incident data encapsulated in an IncidentPayload object
 */
package com.disa.shelter_service.event;

import java.time.LocalDateTime;

public class IncidentEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private IncidentPayload payload;

    public IncidentEvent() {}

    public IncidentEvent(String eventType, LocalDateTime timestamp, IncidentPayload payload) {
        this.eventType = eventType;
        this.timestamp = timestamp;
        this.payload = payload;
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public IncidentPayload getPayload() { return payload; }
    public void setPayload(IncidentPayload payload) { this.payload = payload; }

    @Override
    public String toString() {
        return "IncidentEvent{eventType='" + eventType + "', timestamp=" + timestamp + ", payload=" + payload + "}";
    }
}
