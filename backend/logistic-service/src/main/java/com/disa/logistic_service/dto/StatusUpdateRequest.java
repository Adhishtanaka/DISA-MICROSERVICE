package com.disa.logistic_service.dto;
import com.disa.logistic_service.entity.MissionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/** * Data Transfer Object for updating the status of a Mission.
 * This DTO encapsulates the necessary information for status update operations
 * without exposing internal entity details.
 * * @author Dilina Mewan
 * @version 1.0
 */
@Data
public class StatusUpdateRequest {
    private MissionStatus status;
}