/**
 * Event class for resource-related messaging.
 * Contains event type, timestamp, and payload with resource details.
 */
package com.disa.resource_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceEvent {
    private String eventType;
    private LocalDateTime timestamp;
    private ResourcePayload payload;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ResourcePayload {
    private Long resourceId;
    private String resourceCode;
    private String name;
    private Integer currentStock;
    private Integer threshold;
    private String location;
}