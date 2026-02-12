/*
 * Escalation Payload
 *
 * Event payload for incident escalation events.
 * Contains escalation details including severity changes.
 *
 * @author Generated
 * @version 1.0
 */
package com.disa.incident_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EscalationPayload {
    private Long incidentId;
    private String incidentCode;
    private String previousSeverity;
    private String newSeverity;
    private String reason;
}