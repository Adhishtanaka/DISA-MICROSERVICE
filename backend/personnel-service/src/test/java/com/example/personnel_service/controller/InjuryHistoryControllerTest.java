package com.example.personnel_service.controller;

import com.example.personnel_service.entity.InjuryHistory;
import com.example.personnel_service.service.InjuryService;
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
class InjuryHistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InjuryService injuryService;

    @InjectMocks
    private InjuryHistoryController injuryHistoryController;

    private ObjectMapper objectMapper;
    private InjuryHistory injury1;
    private InjuryHistory injury2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(injuryHistoryController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        injury1 = new InjuryHistory();
        injury1.setId(1L);
        injury1.setInjuryType("Fracture");
        injury1.setRecoveryStatus("Recovered");
        injury1.setRestrictions("None");
        injury1.setDisabled(false);

        injury2 = new InjuryHistory();
        injury2.setId(2L);
        injury2.setInjuryType("Burn");
        injury2.setRecoveryStatus("In Progress");
        injury2.setRestrictions("Light duty only");
        injury2.setDisabled(false);
    }

    @Test
    void getAllInjuryHistories_ShouldReturnList() throws Exception {
        when(injuryService.getAllInjuryHistories()).thenReturn(Arrays.asList(injury1, injury2));

        mockMvc.perform(get("/api/personnel/injury-histories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].injuryType").value("Fracture"))
                .andExpect(jsonPath("$[1].injuryType").value("Burn"));

        verify(injuryService).getAllInjuryHistories();
    }

    @Test
    void getInjuryHistoryById_ShouldReturnInjury() throws Exception {
        when(injuryService.getInjuryHistoryById(1L)).thenReturn(injury1);

        mockMvc.perform(get("/api/personnel/injury-histories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.injuryType").value("Fracture"))
                .andExpect(jsonPath("$.recoveryStatus").value("Recovered"));

        verify(injuryService).getInjuryHistoryById(1L);
    }

    @Test
    void getInjuryHistoryById_NotFound_ShouldReturn500() throws Exception {
        when(injuryService.getInjuryHistoryById(99L))
                .thenThrow(new RuntimeException("InjuryHistory not found"));

        mockMvc.perform(get("/api/personnel/injury-histories/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createInjuryHistories_ShouldReturnCreated() throws Exception {
        List<InjuryHistory> injuries = Arrays.asList(injury1, injury2);
        when(injuryService.addInjuryHistory(any())).thenReturn(injuries);

        mockMvc.perform(post("/api/personnel/injury-histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(injuries)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(injuryService).addInjuryHistory(any());
    }

    @Test
    void updateInjuryHistories_ShouldReturnUpdated() throws Exception {
        injury1.setRecoveryStatus("Fully Recovered");
        List<InjuryHistory> injuries = Arrays.asList(injury1);
        when(injuryService.updateInjuryHistory(any())).thenReturn(injuries);

        mockMvc.perform(put("/api/personnel/injury-histories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(injuries)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recoveryStatus").value("Fully Recovered"));

        verify(injuryService).updateInjuryHistory(any());
    }

    @Test
    void softDeleteInjuryHistory_ShouldReturnDisabled() throws Exception {
        injury1.setDisabled(true);
        when(injuryService.softDeleteInjuryHistory(1L)).thenReturn(injury1);

        mockMvc.perform(patch("/api/personnel/injury-histories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(injuryService).softDeleteInjuryHistory(1L);
    }

    @Test
    void deleteInjuryHistory_ShouldReturnNoContent() throws Exception {
        doNothing().when(injuryService).deleteInjuryHistory(1L);

        mockMvc.perform(delete("/api/personnel/injury-histories/1"))
                .andExpect(status().isNoContent());

        verify(injuryService).deleteInjuryHistory(1L);
    }
}
