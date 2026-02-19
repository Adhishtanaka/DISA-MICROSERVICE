/*
 * AssessmentEvent is the inbound message received from the assessment service
 * via RabbitMQ when a disaster assessment is completed. The embedded payload
 * contains required actions used to auto-generate follow-up response tasks.
 */
package com.disa.task_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private AssessmentPayload payload;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssessmentPayload {
        private Long incidentId;
        private String location;
        private List<String> requiredActions;
        private String severity;
    }
}