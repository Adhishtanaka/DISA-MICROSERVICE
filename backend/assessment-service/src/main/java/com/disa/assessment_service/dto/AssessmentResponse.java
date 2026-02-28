package com.disa.assessment_service.dto;

import com.disa.assessment_service.entity.AssessmentStatus;
import com.disa.assessment_service.entity.DamageSeverity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResponse {
    private Long id;
    private String assessmentCode;
    private Long incidentId;
    private Long assessorId;
    private String assessorName;
    private DamageSeverity severity;
    private String findings;
    private String recommendations;
    private List<String> requiredActions;
    private List<String> photoUrls;
    private AssessmentStatus status;
    private Integer estimatedCasualties;
    private Integer estimatedDisplaced;
    private String affectedInfrastructure;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}
