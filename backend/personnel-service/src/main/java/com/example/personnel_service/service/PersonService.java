package com.example.personnel_service.service;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> getAllPersons() {
        return repository.findAll();
    }

    public Person getPersonById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    public List<Person> addPerson(List<Person> persons) {
        return repository.saveAll(persons);
    }

    public List<Person> updatePerson(List<Person> persons) {
        return repository.saveAll(persons);
    }

    public Person softDeletePerson(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        person.setDisabled(true);
        return repository.save(person);
    }

    public void deletePerson(Long id) {
        Person person = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        repository.delete(person);
    }
}
