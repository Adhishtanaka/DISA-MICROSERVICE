package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.personnel_service.exception.GlobalExceptionHandler;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private ObjectMapper objectMapper;
    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(personController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        person1 = new Person();
        person1.setId(1L);
        person1.setPersonalCode("PER-001");
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setPhone("0771234567");
        person1.setEmail("john@example.com");
        person1.setAddress("Colombo");
        person1.setRole("Rescuer");
        person1.setDepartment("Operations");
        person1.setOrganization("DISA");
        person1.setRank("Senior");
        person1.setStatus("Available");
        person1.setDisabled(false);

        person2 = new Person();
        person2.setId(2L);
        person2.setPersonalCode("PER-002");
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setPhone("0779876543");
        person2.setEmail("jane@example.com");
        person2.setAddress("Kandy");
        person2.setRole("Medic");
        person2.setDepartment("Medical");
        person2.setOrganization("DISA");
        person2.setRank("Junior");
        person2.setStatus("Available");
        person2.setDisabled(false);
    }

    @Test
    void getAllPersons_ShouldReturnListOfPersons() throws Exception {
        List<Person> persons = Arrays.asList(person1, person2);
        when(personService.getAllPersons()).thenReturn(persons);

        mockMvc.perform(get("/api/personnel/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(personService).getAllPersons();
    }

    @Test
    void getAllPersons_EmptyList_ShouldReturnEmptyArray() throws Exception {
        when(personService.getAllPersons()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/personnel/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getPersonById_ShouldReturnPerson() throws Exception {
        when(personService.getPersonById(1L)).thenReturn(person1);

        mockMvc.perform(get("/api/personnel/person/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.role").value("Rescuer"));

        verify(personService).getPersonById(1L);
    }

    @Test
    void getPersonById_NotFound_ShouldReturn500() throws Exception {
        when(personService.getPersonById(99L)).thenThrow(new RuntimeException("Person not found"));

        mockMvc.perform(get("/api/personnel/person/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createPersons_ShouldReturnCreatedPersons() throws Exception {
        List<Person> persons = Arrays.asList(person1, person2);
        when(personService.addPerson(any())).thenReturn(persons);

        mockMvc.perform(post("/api/personnel/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persons)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].personalCode").value("PER-001"));

        verify(personService).addPerson(any());
    }

    @Test
    void updatePersons_ShouldReturnUpdatedPersons() throws Exception {
        person1.setStatus("On Duty");
        List<Person> persons = Arrays.asList(person1);
        when(personService.updatePerson(any())).thenReturn(persons);

        mockMvc.perform(put("/api/personnel/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(persons)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("On Duty"));

        verify(personService).updatePerson(any());
    }

    @Test
    void softDeletePerson_ShouldReturnDisabledPerson() throws Exception {
        person1.setDisabled(true);
        when(personService.softDeletePerson(1L)).thenReturn(person1);

        mockMvc.perform(patch("/api/personnel/person/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(personService).softDeletePerson(1L);
    }

    @Test
    void deletePerson_ShouldReturnNoContent() throws Exception {
        doNothing().when(personService).deletePerson(1L);

        mockMvc.perform(delete("/api/personnel/person/1"))
                .andExpect(status().isNoContent());

        verify(personService).deletePerson(1L);
    }
}
