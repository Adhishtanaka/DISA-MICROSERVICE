/*
 * Incident Request DTO
 *
 * Data Transfer Object for incident creation and update requests.
 * Contains validation annotations for input validation.
 */

package com.disa.incident_service.dto;

import com.disa.incident_service.entity.IncidentType;
import com.disa.incident_service.entity.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IncidentRequest {
    @NotNull
    private IncidentType type;

    @NotNull
    private Severity severity;

    @NotBlank
    private String description;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotBlank
    private String address;
}