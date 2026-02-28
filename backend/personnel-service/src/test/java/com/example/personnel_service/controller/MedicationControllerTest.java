package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Medication;
import com.example.personnel_service.service.MedicationService;
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
class MedicationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicationService medicationService;

    @InjectMocks
    private MedicationController medicationController;

    private ObjectMapper objectMapper;
    private Medication med1;
    private Medication med2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(medicationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        med1 = new Medication();
        med1.setId(1L);
        med1.setName("Ibuprofen");
        med1.setDosage("400mg");
        med1.setFrequency("Twice daily");
        med1.setTreated(true);
        med1.setDisabled(false);

        med2 = new Medication();
        med2.setId(2L);
        med2.setName("Metformin");
        med2.setDosage("500mg");
        med2.setFrequency("Once daily");
        med2.setTreated(false);
        med2.setDisabled(false);
    }

    @Test
    void getAllMedications_ShouldReturnList() throws Exception {
        when(medicationService.getAllMedications()).thenReturn(Arrays.asList(med1, med2));

        mockMvc.perform(get("/api/personnel/medications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Ibuprofen"))
                .andExpect(jsonPath("$[1].name").value("Metformin"));

        verify(medicationService).getAllMedications();
    }

    @Test
    void getMedicationById_ShouldReturnMedication() throws Exception {
        when(medicationService.getMedicationById(1L)).thenReturn(med1);

        mockMvc.perform(get("/api/personnel/medications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ibuprofen"))
                .andExpect(jsonPath("$.dosage").value("400mg"));

        verify(medicationService).getMedicationById(1L);
    }

    @Test
    void getMedicationById_NotFound_ShouldReturn500() throws Exception {
        when(medicationService.getMedicationById(99L))
                .thenThrow(new RuntimeException("Medication not found"));

        mockMvc.perform(get("/api/personnel/medications/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createMedications_ShouldReturnCreated() throws Exception {
        List<Medication> meds = Arrays.asList(med1, med2);
        when(medicationService.addMedication(any())).thenReturn(meds);

        mockMvc.perform(post("/api/personnel/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meds)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(medicationService).addMedication(any());
    }

    @Test
    void updateMedications_ShouldReturnUpdated() throws Exception {
        med1.setDosage("600mg");
        List<Medication> meds = Arrays.asList(med1);
        when(medicationService.updateMedication(any())).thenReturn(meds);

        mockMvc.perform(put("/api/personnel/medications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(meds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].dosage").value("600mg"));

        verify(medicationService).updateMedication(any());
    }

    @Test
    void softDeleteMedication_ShouldReturnDisabled() throws Exception {
        med1.setDisabled(true);
        when(medicationService.softDeleteMedication(1L)).thenReturn(med1);

        mockMvc.perform(patch("/api/personnel/medications/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(medicationService).softDeleteMedication(1L);
    }

    @Test
    void deleteMedication_ShouldReturnNoContent() throws Exception {
        doNothing().when(medicationService).deleteMedication(1L);

        mockMvc.perform(delete("/api/personnel/medications/1"))
                .andExpect(status().isNoContent());

        verify(medicationService).deleteMedication(1L);
    }
}
