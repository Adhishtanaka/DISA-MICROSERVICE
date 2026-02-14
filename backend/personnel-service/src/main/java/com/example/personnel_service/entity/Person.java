package com.example.personnel_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String personalCode;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<EmergencyContact> emergencyContacts;

    private String role;
    private String department;
    private String organization;
    private String rank;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "medical_condition_id")
    private MedicalCondition medicalCondition;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
    private List<Skill> skills;

    private String status;
    private LocalDateTime shiftStartTime;
    private LocalDateTime shiftEndTime;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private boolean isDisabled;
}
