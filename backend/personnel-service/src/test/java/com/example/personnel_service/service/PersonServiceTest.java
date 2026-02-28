package com.example.personnel_service.service;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.messaging.MessagePublisher;
import com.example.personnel_service.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository repository;

    @Mock
    private MessagePublisher messagePublisher;

    @InjectMocks
    private PersonService personService;

    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        person1 = new Person();
        person1.setId(1L);
        person1.setPersonalCode("PER-001");
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setRole("Rescuer");
        person1.setStatus("Available");
        person1.setDisabled(false);

        person2 = new Person();
        person2.setId(2L);
        person2.setPersonalCode("PER-002");
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setRole("Medic");
        person2.setStatus("Available");
        person2.setDisabled(false);
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        when(repository.findAll()).thenReturn(Arrays.asList(person1, person2));

        List<Person> result = personService.getAllPersons();

        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(repository).findAll();
    }

    @Test
    void getAllPersons_EmptyList_ShouldReturnEmpty() {
        when(repository.findAll()).thenReturn(Arrays.asList());

        List<Person> result = personService.getAllPersons();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPersonById_ShouldReturnPerson() {
        when(repository.findById(1L)).thenReturn(Optional.of(person1));

        Person result = personService.getPersonById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("PER-001", result.getPersonalCode());
        verify(repository).findById(1L);
    }

    @Test
    void getPersonById_NotFound_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> personService.getPersonById(99L));

        assertEquals("Person not found", exception.getMessage());
    }

    @Test
    void addPerson_ShouldSaveAndPublishEvents() {
        List<Person> persons = Arrays.asList(person1, person2);
        when(repository.saveAll(persons)).thenReturn(persons);
        doNothing().when(messagePublisher).publishPersonnelStatusEvent(any());

        List<Person> result = personService.addPerson(persons);

        assertEquals(2, result.size());
        verify(repository).saveAll(persons);
        verify(messagePublisher, times(2)).publishPersonnelStatusEvent(any());
    }

    @Test
    void addPerson_EventPublishingFails_ShouldStillReturnPersons() {
        List<Person> persons = Arrays.asList(person1);
        when(repository.saveAll(persons)).thenReturn(persons);
        doThrow(new RuntimeException("RabbitMQ down")).when(messagePublisher).publishPersonnelStatusEvent(any());

        List<Person> result = personService.addPerson(persons);

        assertEquals(1, result.size());
        verify(repository).saveAll(persons);
    }

    @Test
    void updatePerson_ShouldSaveAndPublishEvents() {
        person1.setStatus("On Duty");
        List<Person> persons = Arrays.asList(person1);
        when(repository.saveAll(persons)).thenReturn(persons);
        doNothing().when(messagePublisher).publishPersonnelStatusEvent(any());

        List<Person> result = personService.updatePerson(persons);

        assertEquals(1, result.size());
        assertEquals("On Duty", result.get(0).getStatus());
        verify(repository).saveAll(persons);
        verify(messagePublisher).publishPersonnelStatusEvent(any());
    }

    @Test
    void softDeletePerson_ShouldMarkAsDisabled() {
        when(repository.findById(1L)).thenReturn(Optional.of(person1));
        when(repository.save(any(Person.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Person result = personService.softDeletePerson(1L);

        assertTrue(result.isDisabled());
        verify(repository).findById(1L);
        verify(repository).save(any(Person.class));
    }

    @Test
    void softDeletePerson_NotFound_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> personService.softDeletePerson(99L));
    }

    @Test
    void deletePerson_ShouldDeleteAndPublishEvent() {
        when(repository.findById(1L)).thenReturn(Optional.of(person1));
        doNothing().when(messagePublisher).publishPersonnelStatusEvent(any());
        doNothing().when(repository).delete(person1);

        personService.deletePerson(1L);

        verify(repository).findById(1L);
        verify(messagePublisher).publishPersonnelStatusEvent(any());
        verify(repository).delete(person1);
    }

    @Test
    void deletePerson_NotFound_ShouldThrowException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> personService.deletePerson(99L));
        verify(repository, never()).delete(any());
    }

    @Test
    void deletePerson_EventPublishingFails_ShouldStillDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(person1));
        doThrow(new RuntimeException("RabbitMQ down")).when(messagePublisher).publishPersonnelStatusEvent(any());
        doNothing().when(repository).delete(person1);

        personService.deletePerson(1L);

        verify(repository).delete(person1);
    }
}
