package com.example.personnel_service.controller;

import com.example.personnel_service.entity.MedicalCondition;
import com.example.personnel_service.service.MedicalConditionService;
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
class MedicalConditionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MedicalConditionService medicalConditionService;

    @InjectMocks
    private MedicalConditionController medicalConditionController;

    private ObjectMapper objectMapper;
    private MedicalCondition condition1;
    private MedicalCondition condition2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(medicalConditionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        condition1 = new MedicalCondition();
        condition1.setId(1L);
        condition1.setBloodGroup("O+");
        condition1.setHeight("175cm");
        condition1.setWeight("70kg");
        condition1.setDisabled(false);

        condition2 = new MedicalCondition();
        condition2.setId(2L);
        condition2.setBloodGroup("A-");
        condition2.setHeight("165cm");
        condition2.setWeight("60kg");
        condition2.setDisabled(false);
    }

    @Test
    void getAllMedicalConditions_ShouldReturnList() throws Exception {
        when(medicalConditionService.getAllMedicalConditions()).thenReturn(Arrays.asList(condition1, condition2));

        mockMvc.perform(get("/api/personnel/medical-conditions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].bloodGroup").value("O+"))
                .andExpect(jsonPath("$[1].bloodGroup").value("A-"));

        verify(medicalConditionService).getAllMedicalConditions();
    }

    @Test
    void getMedicalConditionById_ShouldReturnCondition() throws Exception {
        when(medicalConditionService.getMedicalConditionById(1L)).thenReturn(condition1);

        mockMvc.perform(get("/api/personnel/medical-conditions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bloodGroup").value("O+"))
                .andExpect(jsonPath("$.height").value("175cm"));

        verify(medicalConditionService).getMedicalConditionById(1L);
    }

    @Test
    void getMedicalConditionById_NotFound_ShouldReturn500() throws Exception {
        when(medicalConditionService.getMedicalConditionById(99L))
                .thenThrow(new RuntimeException("MedicalCondition not found"));

        mockMvc.perform(get("/api/personnel/medical-conditions/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createMedicalConditions_ShouldReturnCreated() throws Exception {
        List<MedicalCondition> conditions = Arrays.asList(condition1, condition2);
        when(medicalConditionService.addMedicalCondition(any())).thenReturn(conditions);

        mockMvc.perform(post("/api/personnel/medical-conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conditions)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(medicalConditionService).addMedicalCondition(any());
    }

    @Test
    void updateMedicalConditions_ShouldReturnUpdated() throws Exception {
        condition1.setWeight("72kg");
        List<MedicalCondition> conditions = Arrays.asList(condition1);
        when(medicalConditionService.updateMedicalCondition(any())).thenReturn(conditions);

        mockMvc.perform(put("/api/personnel/medical-conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conditions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].weight").value("72kg"));

        verify(medicalConditionService).updateMedicalCondition(any());
    }

    @Test
    void softDeleteMedicalCondition_ShouldReturnDisabled() throws Exception {
        condition1.setDisabled(true);
        when(medicalConditionService.softDeleteMedicalCondition(1L)).thenReturn(condition1);

        mockMvc.perform(patch("/api/personnel/medical-conditions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(medicalConditionService).softDeleteMedicalCondition(1L);
    }

    @Test
    void deleteMedicalCondition_ShouldReturnNoContent() throws Exception {
        doNothing().when(medicalConditionService).deleteMedicalCondition(1L);

        mockMvc.perform(delete("/api/personnel/medical-conditions/1"))
                .andExpect(status().isNoContent());

        verify(medicalConditionService).deleteMedicalCondition(1L);
    }
}
