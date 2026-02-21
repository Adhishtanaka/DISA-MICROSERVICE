package com.example.personnel_service.repository;

import com.example.personnel_service.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Person entity.
 * Extends JpaRepository to provide standard CRUD operations for Person entities.
 * Spring Data JPA automatically provides implementation at runtime.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
public interface PersonRepository extends JpaRepository<Person, Long> {

}
