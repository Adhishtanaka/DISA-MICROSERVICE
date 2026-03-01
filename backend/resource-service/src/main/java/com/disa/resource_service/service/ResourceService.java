/**
 * Service interface for resource business logic operations.
 * Defines methods for managing disaster relief resources and inventory.
 */
package com.disa.resource_service.service;

import com.disa.resource_service.dto.ResourceRequest;
import com.disa.resource_service.dto.ResourceResponse;
import com.disa.resource_service.entity.Resource;
import com.disa.resource_service.entity.ResourceType;

import java.util.List;

public interface ResourceService {
    ResourceResponse createResource(ResourceRequest request);

    ResourceResponse getResourceById(Long id);

    List<ResourceResponse> getAllResources();

    List<ResourceResponse> getResourcesByType(ResourceType type);

    List<ResourceResponse> getLowStockResources();

    ResourceResponse updateResource(Long id, ResourceRequest request);

    ResourceResponse updateStock(Long id, Integer quantity);

    ResourceResponse incrementStock(Long id, Integer quantity);

    ResourceResponse decrementStock(Long id, Integer quantity);

    void deleteResource(Long id);
}