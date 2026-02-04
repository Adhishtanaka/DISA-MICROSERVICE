/*
 * AssessmentEvent for task service
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