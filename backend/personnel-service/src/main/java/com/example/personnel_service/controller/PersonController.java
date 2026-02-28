package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing Person entities.
 * Provides endpoints for CRUD operations on personnel records.
 * Base path: /api/personnel/person
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@RestController
@RequestMapping("/api/personnel/person")
public class PersonController {
    private final PersonService personService;

    /**
     * Constructs a new PersonController with the specified PersonService.
     * 
     * @param personService the service used to handle Person business logic
     */
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Retrieves all persons from the database.
     * 
     * @return ResponseEntity containing a list of all Person entities
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    /**
     * Retrieves a specific person by their unique identifier.
     * 
     * @param id the unique identifier of the person to retrieve
     * @return ResponseEntity containing the Person entity with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    /**
     * Creates one or more new person records.
     * 
     * @param persons list of Person entities to create
     * @return ResponseEntity containing the created Person entities with HTTP status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<List<Person>> createPersons(@RequestBody List<Person> persons) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.addPerson(persons));
    }

    /**
     * Updates one or more existing person records.
     * 
     * @param persons list of Person entities with updated information
     * @return ResponseEntity containing the updated Person entities
     */
    @PutMapping
    public ResponseEntity<List<Person>> updatePersons(@RequestBody List<Person> persons) {
        return ResponseEntity.ok(personService.updatePerson(persons));
    }

    /**
     * Performs a soft delete on a person by marking them as disabled.
     * The person record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the person to soft delete
     * @return ResponseEntity containing the soft-deleted Person entity
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Person> softDeletePerson(@PathVariable Long id) {
        return ResponseEntity.ok(personService.softDeletePerson(id));
    }

    /**
     * Permanently deletes a person record from the database.
     * 
     * @param id the unique identifier of the person to delete
     * @return ResponseEntity with HTTP status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
