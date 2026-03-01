/**
 * JPA Entity representing a disaster relief resource.
 * Maps to the 'resource' table in the database with fields for inventory management.
 */
package com.disa.resource_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resourceCode; // RES-101

    @Enumerated(EnumType.STRING)
    private ResourceType type; // FOOD, WATER, MEDICINE, EQUIPMENT

    private String name;
    private String description;

    private Integer currentStock;
    private Integer threshold; // Alert when below this level
    private String unit; // kg, liters, pieces, boxes

    private String location; // Warehouse/storage location

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}