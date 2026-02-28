package com.example.personnel_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entity representing physical limitations of personnel.
 * Tracks physical constraints that may affect task assignments.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Entity
@Data
public class PhysicalLimitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String limitation;

    @ManyToOne
    @JoinColumn(name = "medical_condition_id")
    private MedicalCondition medicalCondition;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;
}
