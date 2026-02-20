package com.disa.mission_service.dto;
import com.disa.mission_service.entity.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
/** * Data Transfer Object for updating the status of a Mission.
 * This DTO encapsulates the necessary information for status update operations
 * without exposing internal entity details.
 * * @author Dilina Mewan
 * @version 1.0
 */
@Data
public class StatusUpdateRequest {
    @NotNull(message = "Mission ID is required")
    private Long missionId;

    @NotNull(message = "Status is required")
    private MissionStatus status;

    private String remarks;
}