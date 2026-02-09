package com.disa.logistic_service.dto;
import com.disa.logistic_service.entity.MissionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data Transfer Object for creating or updating a Mission.
 * This DTO encapsulates the necessary information for mission operations
 * without exposing internal entity details.
 * * @author Dilina Mewan
 * @version 1.0
 */
@Data
public class MissionRequest {
    private MissionType type;
    private String origin;
    private String destination;
    private String description;
    private String cargoDetails;
    private String vehicleType;
}