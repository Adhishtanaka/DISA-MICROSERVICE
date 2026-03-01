/**
 * Implementation of ResourceService interface.
 * Handles business logic for resource management, stock operations, and event publishing.
 */
package com.disa.resource_service.service;

import com.disa.resource_service.dto.ResourceRequest;
import com.disa.resource_service.dto.ResourceResponse;
import com.disa.resource_service.entity.Resource;
import com.disa.resource_service.entity.ResourceType;
import com.disa.resource_service.event.EventPublisher;
import com.disa.resource_service.exception.ResourceNotFoundException;
import com.disa.resource_service.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public ResourceResponse createResource(ResourceRequest request) {
        Resource resource = new Resource();
        resource.setResourceCode(request.getResourceCode());
        resource.setType(request.getType());
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setCurrentStock(request.getCurrentStock());
        resource.setThreshold(request.getThreshold());
        resource.setUnit(request.getUnit());
        resource.setLocation(request.getLocation());

        Resource saved = resourceRepository.save(resource);
        log.info("Created resource: {}", saved.getResourceCode());
        return mapToResponse(saved);
    }

    @Override
    public ResourceResponse getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));
        return mapToResponse(resource);
    }

    @Override
    public List<ResourceResponse> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceResponse> getResourcesByType(ResourceType type) {
        return resourceRepository.findByType(type).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResourceResponse> getLowStockResources() {
        return resourceRepository.findAll().stream()
                .filter(r -> r.getCurrentStock() < r.getThreshold())
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResourceResponse updateResource(Long id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        resource.setResourceCode(request.getResourceCode());
        resource.setType(request.getType());
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setCurrentStock(request.getCurrentStock());
        resource.setThreshold(request.getThreshold());
        resource.setUnit(request.getUnit());
        resource.setLocation(request.getLocation());

        Resource saved = resourceRepository.save(resource);
        checkAndPublishLowStockEvent(saved);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ResourceResponse updateStock(Long id, Integer quantity) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        resource.setCurrentStock(quantity);
        Resource saved = resourceRepository.save(resource);
        checkAndPublishLowStockEvent(saved);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ResourceResponse incrementStock(Long id, Integer quantity) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        resource.setCurrentStock(resource.getCurrentStock() + quantity);
        Resource saved = resourceRepository.save(resource);
        checkAndPublishLowStockEvent(saved);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public ResourceResponse decrementStock(Long id, Integer quantity) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + id));

        if (resource.getCurrentStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock");
        }
        resource.setCurrentStock(resource.getCurrentStock() - quantity);
        Resource saved = resourceRepository.save(resource);
        checkAndPublishLowStockEvent(saved);
        return mapToResponse(saved);
    }

    @Override
    @Transactional
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found with id: " + id);
        }
        resourceRepository.deleteById(id);
        log.info("Deleted resource with id: {}", id);
    }

    private void checkAndPublishLowStockEvent(Resource resource) {
        if (resource.getCurrentStock() < resource.getThreshold()) {
            eventPublisher.publishCriticalLowStock(resource);
        }
    }

    private ResourceResponse mapToResponse(Resource resource) {
        return new ResourceResponse(
                resource.getId(),
                resource.getResourceCode(),
                resource.getType(),
                resource.getName(),
                resource.getDescription(),
                resource.getCurrentStock(),
                resource.getThreshold(),
                resource.getUnit(),
                resource.getLocation(),
                resource.getCreatedAt(),
                resource.getUpdatedAt());
    }
}