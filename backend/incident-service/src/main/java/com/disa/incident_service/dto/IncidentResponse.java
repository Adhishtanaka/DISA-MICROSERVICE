/*
 * Incident Response DTO
 *
 * Data Transfer Object for incident API responses.
 * Contains all incident details for client consumption.
 */

package com.disa.incident_service.dto;

import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.entity.IncidentType;
import com.disa.incident_service.entity.Severity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IncidentResponse {
    private Long id;
    private String incidentCode;
    private IncidentType type;
    private Severity severity;
    private IncidentStatus status;
    private String description;
    private Double latitude;
    private Double longitude;
    private String address;
    private LocalDateTime reportedAt;
    private LocalDateTime updatedAt;
}