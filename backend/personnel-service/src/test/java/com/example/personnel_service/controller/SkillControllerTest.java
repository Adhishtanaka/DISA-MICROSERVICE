package com.example.personnel_service.controller;

import com.example.personnel_service.entity.Skill;
import com.example.personnel_service.service.SkillService;
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
class SkillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private SkillController skillController;

    private ObjectMapper objectMapper;
    private Skill skill1;
    private Skill skill2;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(skillController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        skill1 = new Skill();
        skill1.setId(1L);
        skill1.setExperienceYears(5);
        skill1.setMissionCount(10);
        skill1.setProfession("Search and Rescue");
        skill1.setLevel("Expert");
        skill1.setDisabled(false);

        skill2 = new Skill();
        skill2.setId(2L);
        skill2.setExperienceYears(3);
        skill2.setMissionCount(5);
        skill2.setProfession("Medical Aid");
        skill2.setLevel("Intermediate");
        skill2.setDisabled(false);
    }

    @Test
    void getAllSkills_ShouldReturnListOfSkills() throws Exception {
        List<Skill> skills = Arrays.asList(skill1, skill2);
        when(skillService.getAllSkills()).thenReturn(skills);

        mockMvc.perform(get("/api/personnel/skills"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].profession").value("Search and Rescue"))
                .andExpect(jsonPath("$[1].profession").value("Medical Aid"));

        verify(skillService).getAllSkills();
    }

    @Test
    void getSkillById_ShouldReturnSkill() throws Exception {
        when(skillService.getSkillById(1L)).thenReturn(skill1);

        mockMvc.perform(get("/api/personnel/skills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profession").value("Search and Rescue"))
                .andExpect(jsonPath("$.experienceYears").value(5))
                .andExpect(jsonPath("$.level").value("Expert"));

        verify(skillService).getSkillById(1L);
    }

    @Test
    void getSkillById_NotFound_ShouldReturn500() throws Exception {
        when(skillService.getSkillById(99L)).thenThrow(new RuntimeException("Skill not found"));

        mockMvc.perform(get("/api/personnel/skills/99"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void createSkills_ShouldReturnCreatedSkills() throws Exception {
        List<Skill> skills = Arrays.asList(skill1, skill2);
        when(skillService.addSkill(any())).thenReturn(skills);

        mockMvc.perform(post("/api/personnel/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skills)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(2));

        verify(skillService).addSkill(any());
    }

    @Test
    void updateSkills_ShouldReturnUpdatedSkills() throws Exception {
        skill1.setLevel("Master");
        List<Skill> skills = Arrays.asList(skill1);
        when(skillService.updateSkill(any())).thenReturn(skills);

        mockMvc.perform(put("/api/personnel/skills")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skills)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].level").value("Master"));

        verify(skillService).updateSkill(any());
    }

    @Test
    void softDeleteSkill_ShouldReturnDisabledSkill() throws Exception {
        skill1.setDisabled(true);
        when(skillService.softDeleteSkill(1L)).thenReturn(skill1);

        mockMvc.perform(patch("/api/personnel/skills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.disabled").value(true));

        verify(skillService).softDeleteSkill(1L);
    }

    @Test
    void deleteSkill_ShouldReturnNoContent() throws Exception {
        doNothing().when(skillService).deleteSkill(1L);

        mockMvc.perform(delete("/api/personnel/skills/1"))
                .andExpect(status().isNoContent());

        verify(skillService).deleteSkill(1L);
    }
}
