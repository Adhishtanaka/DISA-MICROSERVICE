package com.disa.assessment_service.dto;

import com.disa.assessment_service.entity.AssessmentStatus;
import com.disa.assessment_service.entity.DamageSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRequest {
    @NotNull(message = "Incident ID is required")
    private Long incidentId;
    
    @NotNull(message = "Assessor ID is required")
    private Long assessorId;
    
    @NotBlank(message = "Assessor name is required")
    private String assessorName;
    
    @NotNull(message = "Severity is required")
    private DamageSeverity severity;
    
    @NotBlank(message = "Findings are required")
    private String findings;
    
    private String recommendations;
    
    private List<String> requiredActions;
    
    private Integer estimatedCasualties;
    private Integer estimatedDisplaced;
    private String affectedInfrastructure;
    
    private AssessmentStatus status;
}
