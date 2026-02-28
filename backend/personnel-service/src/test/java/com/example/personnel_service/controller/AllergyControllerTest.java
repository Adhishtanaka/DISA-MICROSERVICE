package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Allergy;
import com.example.personnel_service.service.AllergyService;
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
class AllergyControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AllergyService allergyService;

    @InjectMocks
    private AllergyController allergyController;

    private ObjectMapper objectMapper;
    private Allergy allergy1;
    private Allergy allergy2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(allergyController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        allergy1 = new Allergy();
        allergy1.setId(1L);
        allergy1.setType("Food");
        allergy1.setAllergyTo("Peanuts");
        allergy1.setDisabled(false);

        allergy2 = new Allergy();
        allergy2.setId(2L);
        allergy2.setType("Drug");
        allergy2.setAllergyTo("Penicillin");
        allergy2.setDisabled(false);
    }

    @Test
    void getAllAllergies_ShouldReturnList() throws Exception {
        List<Allergy> allergies = Arrays.asList(allergy1, allergy2);
        when(allergyService.getAllAllergies()).thenReturn(allergies);

        mockMvc.perform(get("/api/personnel/allergies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].type").value("Food"))
                .andExpect(jsonPath("$[1].allergyTo").value("Penicillin"));

        verify(allergyService).getAllAllergies();
    }

    @Test
    void getAllergyById_ShouldReturnAllergy() throws Exception {
        when(allergyService.getAllergyById(1L)).thenReturn(allergy1);

        mockMvc.perform(get("/api/personnel/allergies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Food"))
                .andExpect(jsonPath("$.allergyTo").value("Peanuts"));

        verify(allergyService).getAllergyById(1L);
    }

    @Test
    void getAllergyById_NotFound_ShouldReturn500() throws Exception {
        when(allergyService.getAllergyById(99L)).thenThrow(new RuntimeException("Allergy not found"));

        mockMvc.perform(get("/api/personnel/allergies/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createAllergies_ShouldReturnCreated() throws Exception {
        List<Allergy> allergies = Arrays.asList(allergy1, allergy2);
        when(allergyService.addAllergy(any())).thenReturn(allergies);

        mockMvc.perform(post("/api/personnel/allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allergies)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(allergyService).addAllergy(any());
    }

    @Test
    void updateAllergies_ShouldReturnUpdated() throws Exception {
        allergy1.setAllergyTo("Shellfish");
        List<Allergy> allergies = Arrays.asList(allergy1);
        when(allergyService.updateAllergy(any())).thenReturn(allergies);

        mockMvc.perform(put("/api/personnel/allergies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(allergies)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].allergyTo").value("Shellfish"));

        verify(allergyService).updateAllergy(any());
    }

    @Test
    void softDeleteAllergy_ShouldReturnDisabled() throws Exception {
        allergy1.setDisabled(true);
        when(allergyService.softDeleteAllergy(1L)).thenReturn(allergy1);

        mockMvc.perform(patch("/api/personnel/allergies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(allergyService).softDeleteAllergy(1L);
    }

    @Test
    void deleteAllergy_ShouldReturnNoContent() throws Exception {
        doNothing().when(allergyService).deleteAllergy(1L);

        mockMvc.perform(delete("/api/personnel/allergies/1"))
                .andExpect(status().isNoContent());

        verify(allergyService).deleteAllergy(1L);
    }
}
