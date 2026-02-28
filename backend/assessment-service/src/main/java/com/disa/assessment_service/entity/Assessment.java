package com.disa.assessment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String assessmentCode;
    
    private Long incidentId;
    private Long assessorId;
    private String assessorName;
    
    @Enumerated(EnumType.STRING)
    private DamageSeverity severity;
    
    @Column(length = 2000)
    private String findings;
    
    @Column(length = 1000)
    private String recommendations;
    
    @ElementCollection
    @CollectionTable(name = "assessment_required_actions", 
                    joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "action")
    private List<String> requiredActions = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "assessment_photos", 
                    joinColumns = @JoinColumn(name = "assessment_id"))
    @Column(name = "photo_url")
    private List<String> photoUrls = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;
    
    private Integer estimatedCasualties;
    private Integer estimatedDisplaced;
    private String affectedInfrastructure;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    private LocalDateTime completedAt;
}
