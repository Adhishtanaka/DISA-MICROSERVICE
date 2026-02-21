package com.example.personnel_service.entity;


import com.example.personnel_service.dto.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entity representing a comprehensive medical condition record for personnel.
 * Contains physical attributes, allergies, chronic conditions, physical limitations,
 * injury history, and current medications.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String bloodGroup;
    private String height;
    private String weight;

    @OneToMany(mappedBy = "medicalCondition", cascade = CascadeType.ALL)
    private List<Allergy> allergies;

    @OneToMany(mappedBy = "medicalCondition", cascade = CascadeType.ALL)
    private List<ChronicCondition> chronicConditions;

    @OneToMany(mappedBy = "medicalCondition", cascade = CascadeType.ALL)
    private List<PhysicalLimitation> physicalLimitations;

    @OneToMany(mappedBy = "medicalCondition", cascade = CascadeType.ALL)
    private List<InjuryHistory> pastInjuries;

    @OneToMany(mappedBy = "medicalCondition", cascade = CascadeType.ALL)
    private List<Medication> medications;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;
}

