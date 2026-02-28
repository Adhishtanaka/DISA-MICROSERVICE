package com.example.personnel_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing a chronic medical condition of personnel.
 * Tracks long-term health conditions and their severity levels.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChronicCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String severity;

    @ManyToOne
    @JoinColumn(name = "medical_condition_id")
    private MedicalCondition medicalCondition;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;
}
