package com.example.personnel_service.controller;

import com.example.personnel_service.entity.EmergencyContact;
import com.example.personnel_service.service.EmergencyContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.personnel_service.exception.GlobalExceptionHandler;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmergencyContactControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmergencyContactService emergencyContactService;

    @InjectMocks
    private EmergencyContactController emergencyContactController;

    private ObjectMapper objectMapper;
    private EmergencyContact contact1;
    private EmergencyContact contact2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(emergencyContactController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        contact1 = new EmergencyContact();
        contact1.setId(1L);
        contact1.setName("Mary Doe");
        contact1.setTelephone("0771112233");
        contact1.setAddress("Colombo");
        contact1.setRelation("Spouse");
        contact1.setNote("Primary contact");
        contact1.setDisabled(false);

        contact2 = new EmergencyContact();
        contact2.setId(2L);
        contact2.setName("Bob Doe");
        contact2.setTelephone("0774445566");
        contact2.setAddress("Kandy");
        contact2.setRelation("Brother");
        contact2.setNote("Secondary contact");
        contact2.setDisabled(false);
    }

    @Test
    void getAllEmergencyContacts_ShouldReturnList() throws Exception {
        when(emergencyContactService.getAllEmergencyContacts()).thenReturn(Arrays.asList(contact1, contact2));

        mockMvc.perform(get("/api/personnel/emergency-contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Mary Doe"))
                .andExpect(jsonPath("$[1].relation").value("Brother"));

        verify(emergencyContactService).getAllEmergencyContacts();
    }

    @Test
    void getEmergencyContactById_ShouldReturnContact() throws Exception {
        when(emergencyContactService.getEmergencyContactById(1L)).thenReturn(contact1);

        mockMvc.perform(get("/api/personnel/emergency-contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mary Doe"))
                .andExpect(jsonPath("$.relation").value("Spouse"));

        verify(emergencyContactService).getEmergencyContactById(1L);
    }

    @Test
    void getEmergencyContactById_NotFound_ShouldReturn500() throws Exception {
        when(emergencyContactService.getEmergencyContactById(99L))
                .thenThrow(new RuntimeException("EmergencyContact not found"));

        mockMvc.perform(get("/api/personnel/emergency-contacts/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createEmergencyContacts_ShouldReturnCreated() throws Exception {
        List<EmergencyContact> contacts = Arrays.asList(contact1, contact2);
        when(emergencyContactService.addEmergencyContact(any())).thenReturn(contacts);

        mockMvc.perform(post("/api/personnel/emergency-contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contacts)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(emergencyContactService).addEmergencyContact(any());
    }

    @Test
    void updateEmergencyContacts_ShouldReturnUpdated() throws Exception {
        contact1.setTelephone("0779999999");
        List<EmergencyContact> contacts = Arrays.asList(contact1);
        when(emergencyContactService.updateEmergencyContact(any())).thenReturn(contacts);

        mockMvc.perform(put("/api/personnel/emergency-contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contacts)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].telephone").value("0779999999"));

        verify(emergencyContactService).updateEmergencyContact(any());
    }

    @Test
    void softDeleteEmergencyContact_ShouldReturnDisabled() throws Exception {
        contact1.setDisabled(true);
        when(emergencyContactService.softDeleteEmergencyContact(1L)).thenReturn(contact1);

        mockMvc.perform(patch("/api/personnel/emergency-contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(emergencyContactService).softDeleteEmergencyContact(1L);
    }

    @Test
    void deleteEmergencyContact_ShouldReturnNoContent() throws Exception {
        doNothing().when(emergencyContactService).deleteEmergencyContact(1L);

        mockMvc.perform(delete("/api/personnel/emergency-contacts/1"))
                .andExpect(status().isNoContent());

        verify(emergencyContactService).deleteEmergencyContact(1L);
    }
}
