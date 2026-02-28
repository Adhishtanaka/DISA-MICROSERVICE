/*
 * Incident Response DTO
 *
 * Data Transfer Object for incident response data sent to clients.
 * Contains all incident information including timestamps.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.dto;

import com.disa.incident_service.entity.IncidentStatus;
import com.disa.incident_service.entity.IncidentType;
import com.disa.incident_service.entity.Severity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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