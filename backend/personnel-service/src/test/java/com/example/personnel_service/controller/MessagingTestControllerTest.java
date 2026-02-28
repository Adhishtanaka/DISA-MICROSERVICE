package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Person;
import com.example.personnel_service.messaging.MessagePublisher;
import com.example.personnel_service.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessagingTestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MessagePublisher messagePublisher;

    @Mock
    private PersonService personService;

    @InjectMocks
    private MessagingTestController messagingTestController;

    private Person person;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(messagingTestController).build();

        person = new Person();
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setRole("Rescuer");
        person.setStatus("Available");
        person.setDisabled(false);
    }

    @Test
    void publishTestStatusEvent_ShouldReturnSuccess() throws Exception {
        when(personService.getPersonById(1L)).thenReturn(person);
        doNothing().when(messagePublisher).publishPersonnelStatusEvent(any());

        mockMvc.perform(post("/api/messaging/test/publish/status/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Test status event published successfully")));

        verify(personService).getPersonById(1L);
        verify(messagePublisher).publishPersonnelStatusEvent(any());
    }

    @Test
    void publishTestStatusEvent_PersonNotFound_ShouldReturn500() throws Exception {
        when(personService.getPersonById(99L)).thenThrow(new RuntimeException("Person not found"));

        mockMvc.perform(post("/api/messaging/test/publish/status/99"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Failed to publish test event")));
    }

    @Test
    void publishTestAvailableEvent_ShouldReturnSuccess() throws Exception {
        when(personService.getPersonById(1L)).thenReturn(person);
        doNothing().when(messagePublisher).publishPersonnelAvailableEvent(any());

        mockMvc.perform(post("/api/messaging/test/publish/available/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Test available event published successfully")));

        verify(messagePublisher).publishPersonnelAvailableEvent(any());
    }

    @Test
    void publishTestAvailableEvent_PersonNotFound_ShouldReturn500() throws Exception {
        when(personService.getPersonById(99L)).thenThrow(new RuntimeException("Person not found"));

        mockMvc.perform(post("/api/messaging/test/publish/available/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void publishCustomEvent_ShouldReturnSuccess() throws Exception {
        when(personService.getPersonById(1L)).thenReturn(person);
        doNothing().when(messagePublisher).publishEvent(any(), any());

        mockMvc.perform(post("/api/messaging/test/publish/custom")
                        .param("routingKey", "custom.key")
                        .param("eventType", "custom.event")
                        .param("personnelId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Custom event published successfully")));

        verify(messagePublisher).publishEvent(eq("custom.key"), any());
    }

    @Test
    void publishCustomEvent_PersonNotFound_ShouldReturn500() throws Exception {
        when(personService.getPersonById(99L)).thenThrow(new RuntimeException("Person not found"));

        mockMvc.perform(post("/api/messaging/test/publish/custom")
                        .param("routingKey", "custom.key")
                        .param("eventType", "custom.event")
                        .param("personnelId", "99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void checkMessagingHealth_ShouldReturnHealthy() throws Exception {
        mockMvc.perform(get("/api/messaging/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("Messaging service is healthy"));
    }
}
