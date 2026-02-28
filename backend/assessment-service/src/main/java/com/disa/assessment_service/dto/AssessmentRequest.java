package com.disa.assessment_service.dto;

import com.disa.assessment_service.entity.AssessmentStatus;
import com.disa.assessment_service.entity.DamageSeverity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentRequest {
    @NotNull
    private Long incidentId;
    
    @NotNull
    private Long assessorId;
    
    @NotNull
    private String assessorName;
    
    @NotNull
    private DamageSeverity severity;
    
    private String findings;
    private String recommendations;
    private List<String> requiredActions;
    private Integer estimatedCasualties;
    private Integer estimatedDisplaced;
    private String affectedInfrastructure;
    private AssessmentStatus status;
}
