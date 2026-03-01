/**
 * Data Transfer Object for stock update operations.
 * Used for incrementing, decrementing, or setting stock levels.
 */
package com.disa.resource_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockUpdateRequest {
    @NotNull
    @Positive
    private Integer quantity;
}