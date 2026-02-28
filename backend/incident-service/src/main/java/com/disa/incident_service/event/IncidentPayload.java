/*
 * Incident Payload
 *
 * Event payload for incident creation events.
 * Contains incident details for messaging to other services.
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
public class IncidentPayload {
    private Long incidentId;
    private String incidentCode;
    private String type;
    private String severity;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
}