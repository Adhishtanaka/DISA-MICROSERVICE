package com.example.personnel_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing medication information for personnel.
 * Tracks medication name, dosage, frequency, and treatment status.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String dosage;
    private String frequency;
    private boolean isTreated;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;

    @ManyToOne
    @JoinColumn(name = "medical_condition_id")
    private MedicalCondition medicalCondition;
}
