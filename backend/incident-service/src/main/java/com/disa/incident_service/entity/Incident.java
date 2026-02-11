/*
 * Incident Entity
 *
 * JPA entity representing a disaster incident.
 * Contains all incident details including location, severity, and status.
 */

package com.disa.incident_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String incidentCode; // INC-001

    @Enumerated(EnumType.STRING)
    private IncidentType type; // EARTHQUAKE, FLOOD, FIRE, LANDSLIDE, TSUNAMI

    @Enumerated(EnumType.STRING)
    private Severity severity; // LOW, MEDIUM, HIGH, CRITICAL

    @Enumerated(EnumType.STRING)
    private IncidentStatus status; // REPORTED, ACTIVE, RESOLVED

    private String description;

    private Double latitude;
    private Double longitude;
    private String address;

    @CreationTimestamp
    private LocalDateTime reportedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}