/**
 * IncidentPayload.java
 *
 * Represents the data payload embedded within an IncidentEvent message received
 * from the RabbitMQ message broker. Contains detailed information about a newly
 * created incident published by the Incident Service.
 *
 * This payload is used by the Shelter Service to determine which shelters are
 * geographically close to the incident and should be prepared for evacuees.
 *
 * Fields:
 *   - incidentId   : Unique identifier of the incident
 *   - incidentCode : Human-readable incident code (e.g., INC-001)
 *   - type         : Category/type of the incident (e.g., flood, fire)
 *   - severity     : Severity level of the incident (e.g., LOW, MEDIUM, HIGH, CRITICAL)
 *   - latitude     : Geographic latitude of the incident location
 *   - longitude    : Geographic longitude of the incident location
 *   - address      : Physical address of the incident
 *   - description  : Detailed description of the incident
 */
package com.disa.shelter_service.event;

public class IncidentPayload {
    private Long incidentId;
    private String incidentCode;
    private String type;
    private String severity;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;

    public IncidentPayload() {}

    public IncidentPayload(Long incidentId, String incidentCode, String type, String severity,
                           Double latitude, Double longitude, String address, String description) {
        this.incidentId = incidentId;
        this.incidentCode = incidentCode;
        this.type = type;
        this.severity = severity;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.description = description;
    }

    public Long getIncidentId() { return incidentId; }
    public void setIncidentId(Long incidentId) { this.incidentId = incidentId; }

    public String getIncidentCode() { return incidentCode; }
    public void setIncidentCode(String incidentCode) { this.incidentCode = incidentCode; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "IncidentPayload{incidentId=" + incidentId + ", incidentCode='" + incidentCode +
               "', severity='" + severity + "', latitude=" + latitude + ", longitude=" + longitude + "}";
    }
}
