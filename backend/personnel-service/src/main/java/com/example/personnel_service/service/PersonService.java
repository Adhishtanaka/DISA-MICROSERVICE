package com.example.personnel_service.service;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Person entities.
 * Provides business logic for Person CRUD operations including soft delete functionality.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
public class PersonService {
    private final PersonRepository repository;

    /**
     * Constructs a new PersonService with the specified repository.
     * 
     * @param repository the PersonRepository used for data access
     */
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all persons from the database.
     * 
     * @return list of all Person entities
     */
    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    /**
     * Retrieves a person by their unique identifier.
     * 
     * @param id the unique identifier of the person
     * @return the Person entity with the specified ID
     * @throws RuntimeException if person with the given ID is not found
     */
    public Person getPersonById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    /**
     * Adds one or more new person records to the database.
     * 
     * @param persons list of Person entities to be saved
     * @return list of saved Person entities with generated IDs
     */
    public List<Person> addPerson(List<Person> persons) {
        return repository.saveAll(persons);
    }

    /**
     * Updates one or more existing person records in the database.
     * 
     * @param persons list of Person entities with updated information
     * @return list of updated Person entities
     */
    public List<Person> updatePerson(List<Person> persons) {
        return repository.saveAll(persons);
    }

    /**
     * Performs a soft delete by marking a person as disabled.
     * The record remains in the database but is flagged as inactive.
     * 
     * @param id the unique identifier of the person to soft delete
     * @return the updated Person entity with isDisabled set to true
     * @throws RuntimeException if person with the given ID is not found
     */
    public Person softDeletePerson(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        person.setDisabled(true);
        return repository.save(person);
    }

    /**
     * Permanently deletes a person record from the database.
     * 
     * @param id the unique identifier of the person to delete
     * @throws RuntimeException if person with the given ID is not found
     */
    public void deletePerson(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        repository.delete(person);
    }
}
