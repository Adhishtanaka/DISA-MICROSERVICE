/*
 * Incident Request DTO
 *
 * Data Transfer Object for creating and updating incident requests.
 * Contains validation annotations for input validation.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.dto;

import com.disa.incident_service.entity.IncidentType;
import com.disa.incident_service.entity.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentRequest {
    @NotNull(message = "Incident type is required")
    private IncidentType type;

    @NotNull(message = "Severity is required")
    private Severity severity;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Latitude is required")
    private Double latitude;

    @NotNull(message = "Longitude is required")
    private Double longitude;

    @NotBlank(message = "Address is required")
    private String address;
}