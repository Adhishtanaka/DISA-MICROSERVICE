/*
 * Escalation Payload
 *
 * Payload class for incident escalation events.
 * Contains details about the severity change and reason.
 */

package com.disa.incident_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EscalationPayload {
    private Long incidentId;
    private String incidentCode;
    private String previousSeverity;
    private String newSeverity;
    private String reason;
}