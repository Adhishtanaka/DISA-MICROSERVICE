package com.example.personnel_service.controller;

import com.example.personnel_service.entity.PhysicalLimitation;
import com.example.personnel_service.service.PhysicalLimitationService;
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
class PhysicalLimitationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PhysicalLimitationService physicalLimitationService;

    @InjectMocks
    private PhysicalLimitationController physicalLimitationController;

    private ObjectMapper objectMapper;
    private PhysicalLimitation limitation1;
    private PhysicalLimitation limitation2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(physicalLimitationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        limitation1 = new PhysicalLimitation();
        limitation1.setId(1L);
        limitation1.setLimitation("Cannot lift heavy objects");
        limitation1.setDisabled(false);

        limitation2 = new PhysicalLimitation();
        limitation2.setId(2L);
        limitation2.setLimitation("Limited mobility in right knee");
        limitation2.setDisabled(false);
    }

    @Test
    void getAllPhysicalLimitations_ShouldReturnList() throws Exception {
        when(physicalLimitationService.getAllPhysicalLimitations())
                .thenReturn(Arrays.asList(limitation1, limitation2));

        mockMvc.perform(get("/api/personnel/physical-limitations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].limitation").value("Cannot lift heavy objects"));

        verify(physicalLimitationService).getAllPhysicalLimitations();
    }

    @Test
    void getPhysicalLimitationById_ShouldReturnLimitation() throws Exception {
        when(physicalLimitationService.getPhysicalLimitationById(1L)).thenReturn(limitation1);

        mockMvc.perform(get("/api/personnel/physical-limitations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.limitation").value("Cannot lift heavy objects"));

        verify(physicalLimitationService).getPhysicalLimitationById(1L);
    }

    @Test
    void getPhysicalLimitationById_NotFound_ShouldReturn500() throws Exception {
        when(physicalLimitationService.getPhysicalLimitationById(99L))
                .thenThrow(new RuntimeException("PhysicalLimitation not found"));

        mockMvc.perform(get("/api/personnel/physical-limitations/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createPhysicalLimitations_ShouldReturnCreated() throws Exception {
        List<PhysicalLimitation> limitations = Arrays.asList(limitation1, limitation2);
        when(physicalLimitationService.addPhysicalLimitation(any())).thenReturn(limitations);

        mockMvc.perform(post("/api/personnel/physical-limitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(limitations)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(physicalLimitationService).addPhysicalLimitation(any());
    }

    @Test
    void updatePhysicalLimitations_ShouldReturnUpdated() throws Exception {
        limitation1.setLimitation("Resolved - no limitations");
        List<PhysicalLimitation> limitations = Arrays.asList(limitation1);
        when(physicalLimitationService.updatePhysicalLimitation(any())).thenReturn(limitations);

        mockMvc.perform(put("/api/personnel/physical-limitations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(limitations)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].limitation").value("Resolved - no limitations"));

        verify(physicalLimitationService).updatePhysicalLimitation(any());
    }

    @Test
    void softDeletePhysicalLimitation_ShouldReturnDisabled() throws Exception {
        limitation1.setDisabled(true);
        when(physicalLimitationService.softDeletePhysicalLimitation(1L)).thenReturn(limitation1);

        mockMvc.perform(patch("/api/personnel/physical-limitations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(physicalLimitationService).softDeletePhysicalLimitation(1L);
    }

    @Test
    void deletePhysicalLimitation_ShouldReturnNoContent() throws Exception {
        doNothing().when(physicalLimitationService).deletePhysicalLimitation(1L);

        mockMvc.perform(delete("/api/personnel/physical-limitations/1"))
                .andExpect(status().isNoContent());

        verify(physicalLimitationService).deletePhysicalLimitation(1L);
    }
}
