package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel/person")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @PostMapping
    public ResponseEntity<List<Person>> createPersons(@RequestBody List<Person> persons) {
        return ResponseEntity.status(HttpStatus.CREATED).body(personService.addPerson(persons));
    }

    @PutMapping
    public ResponseEntity<List<Person>> updatePersons(@RequestBody List<Person> persons) {
        return ResponseEntity.ok(personService.updatePerson(persons));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Person> softDeletePerson(@PathVariable Long id) {
        return ResponseEntity.ok(personService.softDeletePerson(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
