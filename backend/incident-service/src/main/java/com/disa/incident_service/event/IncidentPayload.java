/*
 * Incident Payload
 *
 * Payload class for incident creation events.
 * Contains incident details that are published when an incident is created.
 */

package com.disa.incident_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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