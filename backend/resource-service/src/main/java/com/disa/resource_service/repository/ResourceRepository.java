/**
 * JPA Repository interface for Resource entity operations.
 * Provides CRUD methods and custom queries for resource management.
 */
package com.disa.resource_service.repository;

import com.disa.resource_service.entity.Resource;
import com.disa.resource_service.entity.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByType(ResourceType type);
}