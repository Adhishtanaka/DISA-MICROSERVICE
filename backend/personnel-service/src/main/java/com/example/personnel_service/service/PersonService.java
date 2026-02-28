package com.example.personnel_service.service;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.event.PersonnelStatusEvent;
import com.example.personnel_service.messaging.MessagePublisher;
import com.example.personnel_service.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for managing Person entities.
 * Provides business logic for Person CRUD operations including soft delete functionality.
 * Publishes personnel status events to RabbitMQ for inter-service communication.
 * 
 * @author DISA Team
 * @version 1.0
 * @since 2026-02-21
 */
@Service
@Slf4j
public class PersonService {
    private final PersonRepository repository;
    private final MessagePublisher messagePublisher;

    /**
     * Constructs a new PersonService with the specified repository and message publisher.
     * 
     * @param repository the PersonRepository used for data access
     * @param messagePublisher the MessagePublisher for publishing events to RabbitMQ
     */
    public PersonService(PersonRepository repository, MessagePublisher messagePublisher) {
        this.repository = repository;
        this.messagePublisher = messagePublisher;
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
     * Publishes personnel.status.changed event for each new person.
     * 
     * @param persons list of Person entities to be saved
     * @return list of saved Person entities with generated IDs
     */
    public List<Person> addPerson(List<Person> persons) {
        List<Person> savedPersons = repository.saveAll(persons);
        
        // Publish event for each new person
        savedPersons.forEach(person -> {
            try {
                publishPersonnelStatusEvent(person, "personnel.created");
                log.info("Published personnel.created event for person ID: {}", person.getId());
            } catch (Exception e) {
                log.error("Failed to publish event for new person ID: {}", person.getId(), e);
            }
        });
        
        return savedPersons;
    }

    /**
     * Updates one or more existing person records in the database.
     * Publishes personnel.status.changed event for each updated person.
     * 
     * @param persons list of Person entities with updated information
     * @return list of updated Person entities
     */
    public List<Person> updatePerson(List<Person> persons) {
        List<Person> updatedPersons = repository.saveAll(persons);
        
        // Publish event for each updated person
        updatedPersons.forEach(person -> {
            try {
                publishPersonnelStatusEvent(person, "personnel.updated");
                log.info("Published personnel.updated event for person ID: {}", person.getId());
            } catch (Exception e) {
                log.error("Failed to publish event for updated person ID: {}", person.getId(), e);
            }
        });
        
        return updatedPersons;
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
     * Publishes personnel.deleted event.
     * 
     * @param id the unique identifier of the person to delete
     * @throws RuntimeException if person with the given ID is not found
     */
    public void deletePerson(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        
        // Publish event before deletion
        try {
            publishPersonnelStatusEvent(person, "personnel.deleted");
            log.info("Published personnel.deleted event for person ID: {}", person.getId());
        } catch (Exception e) {
            log.error("Failed to publish event for deleted person ID: {}", person.getId(), e);
        }
        
        repository.delete(person);
    }
    
    /**
     * Creates and publishes a personnel status event.
     * 
     * @param person the Person entity
     * @param eventType the type of event (e.g., "personnel.created", "personnel.updated")
     */
    private void publishPersonnelStatusEvent(Person person, String eventType) {
        PersonnelStatusEvent event = new PersonnelStatusEvent();
        event.setEventType(eventType);
        event.setTimestamp(LocalDateTime.now());
        
        PersonnelStatusEvent.PersonnelStatusPayload payload = new PersonnelStatusEvent.PersonnelStatusPayload();
        payload.setPersonnelId(person.getId());
        payload.setPersonnelCode("PER-" + person.getId());
        payload.setFullName(person.getFirstName() + " " + person.getLastName());
        payload.setStatus(person.isDisabled() ? "UNAVAILABLE" : "AVAILABLE");
        payload.setRole(person.getRole() != null ? person.getRole() : "UNKNOWN");
        payload.setIsAvailable(!person.isDisabled());
        payload.setSkills(person.getSkills() != null && !person.getSkills().isEmpty() 
                ? person.getSkills().stream()
                    .map(skill -> skill.toString())
                    .reduce((s1, s2) -> s1 + ", " + s2)
                    .orElse("")
                : "");
        payload.setNotes("Personnel " + eventType);
        
        event.setPayload(payload);
        
        messagePublisher.publishPersonnelStatusEvent(event);
    }
}
