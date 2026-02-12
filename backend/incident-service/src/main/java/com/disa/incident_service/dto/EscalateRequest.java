/*
 * Escalate Request DTO
 *
 * Data Transfer Object for incident escalation requests.
 * Contains the new severity level and optional reason.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.dto;

import com.disa.incident_service.entity.Severity;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscalateRequest {
    @NotNull(message = "New severity is required")
    private Severity newSeverity;

    private String reason;
}