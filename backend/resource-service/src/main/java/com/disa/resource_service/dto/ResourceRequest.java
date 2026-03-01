/**
 * Data Transfer Object for creating or updating a resource.
 * Contains validation constraints for required fields.
 */
package com.disa.resource_service.dto;

import com.disa.resource_service.entity.ResourceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRequest {
    @NotBlank
    private String resourceCode;

    @NotNull
    private ResourceType type;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Positive
    private Integer currentStock;

    @NotNull
    @Positive
    private Integer threshold;

    @NotBlank
    private String unit;

    @NotBlank
    private String location;
}