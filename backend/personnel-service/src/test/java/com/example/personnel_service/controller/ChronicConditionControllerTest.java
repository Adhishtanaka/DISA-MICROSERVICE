package com.example.personnel_service.controller;

import com.example.personnel_service.entity.ChronicCondition;
import com.example.personnel_service.service.ChronicConditionService;
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
class ChronicConditionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ChronicConditionService chronicConditionService;

    @InjectMocks
    private ChronicConditionController chronicConditionController;

    private ObjectMapper objectMapper;
    private ChronicCondition condition1;
    private ChronicCondition condition2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(chronicConditionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        condition1 = new ChronicCondition();
        condition1.setId(1L);
        condition1.setName("Asthma");
        condition1.setSeverity("Moderate");
        condition1.setDisabled(false);

        condition2 = new ChronicCondition();
        condition2.setId(2L);
        condition2.setName("Diabetes");
        condition2.setSeverity("Mild");
        condition2.setDisabled(false);
    }

    @Test
    void getAllChronicConditions_ShouldReturnList() throws Exception {
        when(chronicConditionService.getAllChronicConditions()).thenReturn(Arrays.asList(condition1, condition2));

        mockMvc.perform(get("/api/personnel/chronic-conditions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Asthma"))
                .andExpect(jsonPath("$[1].name").value("Diabetes"));

        verify(chronicConditionService).getAllChronicConditions();
    }

    @Test
    void getChronicConditionById_ShouldReturnCondition() throws Exception {
        when(chronicConditionService.getChronicConditionById(1L)).thenReturn(condition1);

        mockMvc.perform(get("/api/personnel/chronic-conditions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asthma"))
                .andExpect(jsonPath("$.severity").value("Moderate"));

        verify(chronicConditionService).getChronicConditionById(1L);
    }

    @Test
    void getChronicConditionById_NotFound_ShouldReturn500() throws Exception {
        when(chronicConditionService.getChronicConditionById(99L))
                .thenThrow(new RuntimeException("ChronicCondition not found"));

        mockMvc.perform(get("/api/personnel/chronic-conditions/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createChronicConditions_ShouldReturnCreated() throws Exception {
        List<ChronicCondition> conditions = Arrays.asList(condition1, condition2);
        when(chronicConditionService.addChronicCondition(any())).thenReturn(conditions);

        mockMvc.perform(post("/api/personnel/chronic-conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conditions)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(chronicConditionService).addChronicCondition(any());
    }

    @Test
    void updateChronicConditions_ShouldReturnUpdated() throws Exception {
        condition1.setSeverity("Severe");
        List<ChronicCondition> conditions = Arrays.asList(condition1);
        when(chronicConditionService.updateChronicCondition(any())).thenReturn(conditions);

        mockMvc.perform(put("/api/personnel/chronic-conditions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conditions)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].severity").value("Severe"));

        verify(chronicConditionService).updateChronicCondition(any());
    }

    @Test
    void softDeleteChronicCondition_ShouldReturnDisabled() throws Exception {
        condition1.setDisabled(true);
        when(chronicConditionService.softDeleteChronicCondition(1L)).thenReturn(condition1);

        mockMvc.perform(patch("/api/personnel/chronic-conditions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(chronicConditionService).softDeleteChronicCondition(1L);
    }

    @Test
    void deleteChronicCondition_ShouldReturnNoContent() throws Exception {
        doNothing().when(chronicConditionService).deleteChronicCondition(1L);

        mockMvc.perform(delete("/api/personnel/chronic-conditions/1"))
                .andExpect(status().isNoContent());

        verify(chronicConditionService).deleteChronicCondition(1L);
    }
}
