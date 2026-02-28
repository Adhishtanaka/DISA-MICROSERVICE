package com.example.personnel_service.service;

import com.example.personnel_service.client.TaskClient;
import com.example.personnel_service.dto.*;
import com.example.personnel_service.entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssignmentServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private TaskClient taskClient;

    @InjectMocks
    private AssignmentService assignmentService;

    private Person availablePerson;
    private Person disabledPerson;
    private Person onDutyPerson;
    private TaskDto pendingTask;
    private TaskDto inProgressTask;
    private TaskDto completedTask;

    @BeforeEach
    void setUp() {
        availablePerson = new Person();
        availablePerson.setId(1L);
        availablePerson.setPersonalCode("PER-001");
        availablePerson.setFirstName("John");
        availablePerson.setLastName("Doe");
        availablePerson.setRole("Rescuer");
        availablePerson.setDepartment("Operations");
        availablePerson.setOrganization("DISA");
        availablePerson.setRank("Senior");
        availablePerson.setStatus("Available");
        availablePerson.setDisabled(false);

        disabledPerson = new Person();
        disabledPerson.setId(2L);
        disabledPerson.setFirstName("Jane");
        disabledPerson.setLastName("Smith");
        disabledPerson.setStatus("Available");
        disabledPerson.setDisabled(true);

        onDutyPerson = new Person();
        onDutyPerson.setId(3L);
        onDutyPerson.setFirstName("Bob");
        onDutyPerson.setLastName("Wilson");
        onDutyPerson.setStatus("On Duty");
        onDutyPerson.setDisabled(false);

        pendingTask = new TaskDto();
        pendingTask.setId(1L);
        pendingTask.setTaskCode("TSK-401");
        pendingTask.setType(TaskTypeDto.RESCUE_OPERATION);
        pendingTask.setTitle("Rescue Operation Task");
        pendingTask.setDescription("Operation at Colombo");
        pendingTask.setPriority(PriorityDto.URGENT);
        pendingTask.setLocation("Colombo");
        pendingTask.setStatus(TaskStatusDto.PENDING);

        inProgressTask = new TaskDto();
        inProgressTask.setId(2L);
        inProgressTask.setTaskCode("TSK-402");
        inProgressTask.setType(TaskTypeDto.MEDICAL_AID);
        inProgressTask.setStatus(TaskStatusDto.IN_PROGRESS);

        completedTask = new TaskDto();
        completedTask.setId(3L);
        completedTask.setTaskCode("TSK-403");
        completedTask.setType(TaskTypeDto.DEBRIS_REMOVAL);
        completedTask.setStatus(TaskStatusDto.COMPLETED);
    }

    @Test
    void getAvailablePersons_ShouldFilterAvailableAndEnabled() {
        when(personService.getAllPersons())
                .thenReturn(Arrays.asList(availablePerson, disabledPerson, onDutyPerson));

        List<PersonDto> result = assignmentService.getAvailablePersons();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        verify(personService).getAllPersons();
    }

    @Test
    void getAvailablePersons_NoAvailablePersons_ShouldReturnEmpty() {
        when(personService.getAllPersons())
                .thenReturn(Arrays.asList(disabledPerson, onDutyPerson));

        List<PersonDto> result = assignmentService.getAvailablePersons();

        assertTrue(result.isEmpty());
    }

    @Test
    void getAvailablePersons_AllAvailable_ShouldReturnAll() {
        Person anotherAvailable = new Person();
        anotherAvailable.setId(4L);
        anotherAvailable.setFirstName("Alice");
        anotherAvailable.setLastName("Brown");
        anotherAvailable.setStatus("Available");
        anotherAvailable.setDisabled(false);

        when(personService.getAllPersons())
                .thenReturn(Arrays.asList(availablePerson, anotherAvailable));

        List<PersonDto> result = assignmentService.getAvailablePersons();

        assertEquals(2, result.size());
    }

    @Test
    void getAvailablePersons_ShouldConvertToPersonDto() {
        when(personService.getAllPersons()).thenReturn(Arrays.asList(availablePerson));

        List<PersonDto> result = assignmentService.getAvailablePersons();

        assertEquals(1, result.size());
        PersonDto dto = result.get(0);
        assertEquals(1L, dto.getId());
        assertEquals("PER-001", dto.getPersonalCode());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Rescuer", dto.getRole());
        assertEquals("Operations", dto.getDepartment());
        assertEquals("DISA", dto.getOrganization());
        assertEquals("Senior", dto.getRank());
        assertEquals("Available", dto.getStatus());
    }

    @Test
    void getPendingTasks_ShouldFilterOnlyPending() {
        when(taskClient.fetchTasks())
                .thenReturn(Arrays.asList(pendingTask, inProgressTask, completedTask));

        List<TaskDto> result = assignmentService.getPendingTasks();

        assertEquals(1, result.size());
        assertEquals("TSK-401", result.get(0).getTaskCode());
        assertEquals(TaskStatusDto.PENDING, result.get(0).getStatus());
        verify(taskClient).fetchTasks();
    }

    @Test
    void getPendingTasks_NoPendingTasks_ShouldReturnEmpty() {
        when(taskClient.fetchTasks())
                .thenReturn(Arrays.asList(inProgressTask, completedTask));

        List<TaskDto> result = assignmentService.getPendingTasks();

        assertTrue(result.isEmpty());
    }

    @Test
    void getPendingTasks_AllPending_ShouldReturnAll() {
        TaskDto anotherPending = new TaskDto();
        anotherPending.setId(4L);
        anotherPending.setTaskCode("TSK-404");
        anotherPending.setStatus(TaskStatusDto.PENDING);

        when(taskClient.fetchTasks())
                .thenReturn(Arrays.asList(pendingTask, anotherPending));

        List<TaskDto> result = assignmentService.getPendingTasks();

        assertEquals(2, result.size());
    }

    @Test
    void matchTaskToPerson_NoAvailablePersons_ShouldThrowException() {
        when(personService.getAllPersons()).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> assignmentService.matchTaskToPerson(pendingTask));

        assertEquals("No available persons found for task assignment", exception.getMessage());
    }

    @Test
    void matchAllPendingTasks_NoPendingTasks_ShouldReturnEmpty() {
        when(taskClient.fetchTasks()).thenReturn(Arrays.asList(inProgressTask, completedTask));

        List<TaskAssignmentDto> result = assignmentService.matchAllPendingTasks();

        assertTrue(result.isEmpty());
    }
}
