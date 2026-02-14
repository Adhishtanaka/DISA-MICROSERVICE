package com.disa.assessment_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentPayload {
    private Long assessmentId;
    private String assessmentCode;
    private Long incidentId;
    private String severity;
    private List<String> requiredActions;
    private Long assessorId;
    private String findings;
}
