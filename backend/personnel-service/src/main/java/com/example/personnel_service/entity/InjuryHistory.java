package com.example.personnel_service.entity;

import com.example.personnel_service.dto.DocumentDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * Entity representing a person's injury history record.
 * Tracks past injuries, recovery status, restrictions, and related documents.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InjuryHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String injuryType;
    private LocalDateTime date;
    private String recoveryStatus;
    private String restrictions;

    @OneToMany(mappedBy = "injuryHistory", cascade = CascadeType.ALL)
    private List<Document> pastDocuments;

    @ManyToOne
    @JoinColumn(name = "medical_condition_id")
    private MedicalCondition medicalCondition;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;

}
