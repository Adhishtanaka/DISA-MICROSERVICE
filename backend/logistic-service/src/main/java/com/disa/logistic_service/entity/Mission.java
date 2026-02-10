package com.disa.logistic_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

/**
 * Represents a logistics mission within the disaster management system.
 * This entity tracks the lifecycle of delivery, rescue, and evacuation operations.
 * * @author Dilina Mewan
 * @version 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "missions")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique business identifier for the mission (e.g., MIS-001).
     * Distinct from the database primary key.
     */
    @Column(nullable = false, unique = true)
    private String missionCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionType type;

    // Vehicle and Driver Details
    private String vehicleId;
    private String vehicleType;
    private String driverId;
    private String driverName;

    // Route Information
    private String origin;
    private String destination;

    @Enumerated(EnumType.STRING)
    private MissionStatus status;

    @Column(length = 1000)
    private String description;

    private String cargoDetails;

    // References to external microservices
    private Long incidentId;
    private Long resourceId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    private String notes;
}