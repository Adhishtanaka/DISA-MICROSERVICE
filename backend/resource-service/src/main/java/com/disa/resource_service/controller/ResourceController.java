/**
 * REST controller for managing disaster relief resources.
 * Provides endpoints for CRUD operations, stock management, and low stock alerts.
 */
package com.disa.resource_service.controller;

import com.disa.resource_service.dto.ResourceRequest;
import com.disa.resource_service.dto.ResourceResponse;
import com.disa.resource_service.dto.StockUpdateRequest;
import com.disa.resource_service.entity.Resource;
import com.disa.resource_service.entity.ResourceType;
import com.disa.resource_service.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    @PostMapping
    public ResponseEntity<ResourceResponse> createResource(@Valid @RequestBody ResourceRequest request) {
        ResourceResponse response = resourceService.createResource(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> getAllResources() {
        List<ResourceResponse> resources = resourceService.getAllResources();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponse> getResourceById(@PathVariable Long id) {
        ResourceResponse resource = resourceService.getResourceById(id);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ResourceResponse>> getResourcesByType(@PathVariable ResourceType type) {
        List<ResourceResponse> resources = resourceService.getResourcesByType(type);
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ResourceResponse>> getLowStockResources() {
        List<ResourceResponse> resources = resourceService.getLowStockResources();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> updateResource(@PathVariable Long id,
            @Valid @RequestBody ResourceRequest request) {
        ResourceResponse response = resourceService.updateResource(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<ResourceResponse> updateStock(@PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        ResourceResponse response = resourceService.updateStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/increment")
    public ResponseEntity<ResourceResponse> incrementStock(@PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        ResourceResponse response = resourceService.incrementStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/decrement")
    public ResponseEntity<ResourceResponse> decrementStock(@PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        ResourceResponse response = resourceService.decrementStock(id, request.getQuantity());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}