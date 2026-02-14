package com.disa.assessment_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private AssessmentPayload payload;
}
