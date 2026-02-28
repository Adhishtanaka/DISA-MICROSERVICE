package com.disa.mission_service.dto;
import com.disa.mission_service.entity.enums.MissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Data Transfer Object for creating or updating a Mission.
 * This DTO encapsulates the necessary information for mission operations
 * without exposing internal entity details.
 * * @author Dilina Mewan
 * @version 1.0
 */
@Data
public class MissionRequest {
    @NotNull(message = "Mission type is required")
    private MissionType type;
    @NotBlank(message = "Origin is required")
    private String origin;
    @NotBlank(message = "Destination is required")
    private String destination;
    private String description;
    private String cargoDetails;
    private String vehicleType;
}