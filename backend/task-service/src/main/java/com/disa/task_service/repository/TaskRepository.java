/*
 * TaskRepository provides data access operations for the Task entity via Spring Data JPA.
 * Extends JpaRepository to inherit standard CRUD, pagination, and sorting capabilities
 * backed by the configured PostgreSQL datasource.
 */
package com.disa.task_service.repository;

import com.disa.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}