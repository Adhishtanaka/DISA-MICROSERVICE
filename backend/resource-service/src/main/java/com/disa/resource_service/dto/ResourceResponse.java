/**
 * Data Transfer Object for returning resource information in API responses.
 * Includes all resource details including timestamps.
 */
package com.disa.resource_service.dto;

import com.disa.resource_service.entity.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceResponse {
    private Long id;
    private String resourceCode;
    private ResourceType type;
    private String name;
    private String description;
    private Integer currentStock;
    private Integer threshold;
    private String unit;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}