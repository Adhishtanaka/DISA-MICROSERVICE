package com.example.personnel_service.controller;

import com.example.personnel_service.dto.*;
import com.example.personnel_service.service.AssignmentService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssignmentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AssignmentService assignmentService;

    @InjectMocks
    private AssignmentController assignmentController;

    private ObjectMapper objectMapper;
    private PersonDto person1;
    private TaskDto task1;
    private TaskAssignmentDto assignment1;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(assignmentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        person1 = new PersonDto();
        person1.setId(1L);
        person1.setPersonalCode("PER-001");
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setRole("Rescuer");
        person1.setDepartment("Operations");
        person1.setStatus("Available");

        task1 = new TaskDto();
        task1.setId(1L);
        task1.setTaskCode("TSK-401");
        task1.setType(TaskTypeDto.RESCUE_OPERATION);
        task1.setTitle("RESCUE_OPERATION Task");
        task1.setDescription("Operation at Colombo");
        task1.setPriority(PriorityDto.URGENT);
        task1.setLocation("Colombo");
        task1.setStatus(TaskStatusDto.PENDING);

        assignment1 = new TaskAssignmentDto(person1, task1, "Best match for rescue operation", 95.0);
    }

    @Test
    void getAvailablePersons_ShouldReturnList() throws Exception {
        List<PersonDto> persons = Arrays.asList(person1);
        when(assignmentService.getAvailablePersons()).thenReturn(persons);

        mockMvc.perform(get("/api/personnel/assignments/available-persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].role").value("Rescuer"));

        verify(assignmentService).getAvailablePersons();
    }

    @Test
    void getAvailablePersons_EmptyList_ShouldReturnEmpty() throws Exception {
        when(assignmentService.getAvailablePersons()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/personnel/assignments/available-persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getPendingTasks_ShouldReturnList() throws Exception {
        List<TaskDto> tasks = Arrays.asList(task1);
        when(assignmentService.getPendingTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/personnel/assignments/pending-tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].taskCode").value("TSK-401"))
                .andExpect(jsonPath("$[0].location").value("Colombo"));

        verify(assignmentService).getPendingTasks();
    }

    @Test
    void matchTaskToPerson_ShouldReturnAssignment() throws Exception {
        when(assignmentService.matchTaskToPerson(any(TaskDto.class))).thenReturn(assignment1);

        mockMvc.perform(post("/api/personnel/assignments/match-task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedPerson.firstName").value("John"))
                .andExpect(jsonPath("$.task.taskCode").value("TSK-401"))
                .andExpect(jsonPath("$.assignmentReason").value("Best match for rescue operation"))
                .andExpect(jsonPath("$.matchScore").value(95.0));

        verify(assignmentService).matchTaskToPerson(any(TaskDto.class));
    }

    @Test
    void matchTaskToPerson_ServiceError_ShouldReturn500() throws Exception {
        when(assignmentService.matchTaskToPerson(any(TaskDto.class)))
                .thenThrow(new RuntimeException("No available persons"));

        mockMvc.perform(post("/api/personnel/assignments/match-task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void matchAllPendingTasks_ShouldReturnAssignments() throws Exception {
        List<TaskAssignmentDto> assignments = Arrays.asList(assignment1);
        when(assignmentService.matchAllPendingTasks()).thenReturn(assignments);

        mockMvc.perform(post("/api/personnel/assignments/match-all-pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].matchScore").value(95.0));

        verify(assignmentService).matchAllPendingTasks();
    }

    @Test
    void matchAllPendingTasks_ServiceError_ShouldReturn500() throws Exception {
        when(assignmentService.matchAllPendingTasks())
                .thenThrow(new RuntimeException("Service unavailable"));

        mockMvc.perform(post("/api/personnel/assignments/match-all-pending"))
                .andExpect(status().isInternalServerError());
    }
}
