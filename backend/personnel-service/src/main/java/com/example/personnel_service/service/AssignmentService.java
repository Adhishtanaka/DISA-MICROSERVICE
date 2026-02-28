package com.example.personnel_service.service;

import com.example.personnel_service.client.TaskClient;
import com.example.personnel_service.dto.PersonDto;
import com.example.personnel_service.dto.TaskAssignmentDto;
import com.example.personnel_service.dto.TaskDto;
import com.example.personnel_service.entity.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for intelligently assigning disaster management tasks to available personnel
 * using Google's Gemini AI for optimal matching.
 *
 * <p>This service provides AI-powered task assignment functionality that analyzes
 * personnel qualifications, skills, availability, and task requirements to determine
 * the most suitable person for each task.</p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Filters available personnel based on status and enabled state</li>
 *   <li>Identifies pending tasks requiring assignment</li>
 *   <li>Uses Gemini AI to match tasks with personnel</li>
 *   <li>Provides reasoning and confidence scores for each assignment</li>
 *   <li>Supports batch assignment of multiple tasks</li>
 *   <li>Includes fallback mechanism for error scenarios</li>
 * </ul>
 *
 * <p><b>AI Matching Criteria:</b></p>
 * <ul>
 *   <li>Task type, priority, and location</li>
 *   <li>Personnel role, skills, and experience</li>
 *   <li>Rank and department alignment</li>
 *   <li>Medical conditions (if relevant)</li>
 *   <li>Current availability and shift timing</li>
 * </ul>
 *
 * @author Disaster Management Center
 * @version 1.0
 * @since 2026-02-15
 * @see PersonDto
 * @see TaskDto
 * @see TaskAssignmentDto
 * @see TaskClient
 */
@Service
public class AssignmentService {

    private final PersonService personService;
    private final TaskClient taskClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${model.gemini.api.url}")
    private String geminiApiUrl;

    @Value("${model.gemini.api.key}")
    private String geminiApiKey;

    /**
     * Constructs a new AssignmentService with required dependencies.
     *
     * @param personService the service for managing personnel data
     * @param taskClient the client for fetching task information
     */
    public AssignmentService(PersonService personService, TaskClient taskClient) {
        this.personService = personService;
        this.taskClient = taskClient;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieves all available personnel who are eligible for task assignment.
     *
     * <p>Filters personnel based on the following criteria:</p>
     * <ul>
     *   <li>Status contains "available" (case-insensitive)</li>
     *   <li>Not disabled (isDisabled = false)</li>
     * </ul>
     *
     * @return a list of available personnel as PersonDto objects, never null
     * @see PersonDto
     */
    public List<PersonDto> getAvailablePersons() {
        return personService.getAllPersons().stream()
                .filter(person -> !person.isDisabled())
                .filter(person -> person.getStatus() != null &&
                        person.getStatus().toLowerCase().contains("available"))
                .map(this::convertToPersonDto)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all pending tasks that require personnel assignment.
     *
     * <p>Filters tasks based on status = PENDING.</p>
     *
     * @return a list of pending tasks as TaskDto objects, never null
     * @see TaskDto
     */
    public List<TaskDto> getPendingTasks() {
        return taskClient.fetchTasks().stream()
                .filter(task -> task.getStatus() != null &&
                        task.getStatus().name().equals("PENDING"))
                .collect(Collectors.toList());
    }

    /**
     * Matches a single task with the most suitable person using Gemini AI.
     *
     * <p>This method performs the following steps:</p>
     * <ol>
     *   <li>Retrieves all available personnel</li>
     *   <li>Builds a detailed prompt with task and personnel information</li>
     *   <li>Calls Gemini AI API for intelligent matching</li>
     *   <li>Parses the AI response to extract the selected person</li>
     *   <li>Returns a TaskAssignmentDto with assignment details</li>
     * </ol>
     *
     * <p>The AI considers multiple factors including skills, experience, role,
     * rank, medical conditions, and current availability.</p>
     *
     * @param task the task to be assigned, must not be null
     * @return a TaskAssignmentDto containing the assigned person, task, reasoning, and confidence score
     * @throws RuntimeException if no available persons are found
     * @see TaskAssignmentDto
     */
    public TaskAssignmentDto matchTaskToPerson(TaskDto task) {
        List<PersonDto> availablePersons = getAvailablePersons();

        if (availablePersons.isEmpty()) {
            throw new RuntimeException("No available persons found for task assignment");
        }

        String prompt = buildPrompt(availablePersons, task);
        String geminiResponse = callGeminiApi(prompt);

        return parseGeminiResponse(geminiResponse, availablePersons, task);
    }

    /**
     * Matches all pending tasks with suitable personnel using batch processing.
     *
     * <p>This method iterates through all pending tasks and assigns each to the
     * most suitable person. If an error occurs during assignment of a specific task,
     * it logs the error and continues with the next task.</p>
     *
     * <p><b>Error Handling:</b></p>
     * <ul>
     *   <li>Individual task assignment failures are logged but don't stop the batch</li>
     *   <li>Successfully assigned tasks are added to the result list</li>
     *   <li>Failed assignments are skipped with error message logged to stderr</li>
     * </ul>
     *
     * @return a list of TaskAssignmentDto objects for successfully assigned tasks
     * @see TaskAssignmentDto
     */
    public List<TaskAssignmentDto> matchAllPendingTasks() {
        List<TaskDto> pendingTasks = getPendingTasks();
        List<TaskAssignmentDto> assignments = new ArrayList<>();

        for (TaskDto task : pendingTasks) {
            try {
                TaskAssignmentDto assignment = matchTaskToPerson(task);
                assignments.add(assignment);
            } catch (Exception e) {
                System.err.println("Failed to assign task " + task.getId() + ": " + e.getMessage());
            }
        }

        return assignments;
    }

    /**
     * Builds a comprehensive prompt for Gemini AI with task and personnel details.
     *
     * <p>The prompt includes:</p>
     * <ul>
     *   <li>Task details: ID, code, type, title, description, priority, location</li>
     *   <li>Personnel details: ID, name, role, department, rank, skills, medical conditions, status</li>
     *   <li>Matching instructions and criteria</li>
     *   <li>Expected JSON response format</li>
     * </ul>
     *
     * <p>The prompt is structured to guide the AI to make optimal decisions based on
     * disaster management requirements and personnel capabilities.</p>
     *
     * @param persons the list of available personnel to consider
     * @param task the task requiring assignment
     * @return a formatted string prompt for Gemini AI
     */
    private String buildPrompt(List<PersonDto> persons, TaskDto task) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are an AI assistant for disaster management personnel assignment. ");
        prompt.append("Analyze the following task and available personnel, then select the MOST SUITABLE person for this task.\n\n");

        prompt.append("### TASK DETAILS ###\n");
        prompt.append("Task ID: ").append(task.getId()).append("\n");
        prompt.append("Task Code: ").append(task.getTaskCode()).append("\n");
        prompt.append("Type: ").append(task.getType()).append("\n");
        prompt.append("Title: ").append(task.getTitle()).append("\n");
        prompt.append("Description: ").append(task.getDescription()).append("\n");
        prompt.append("Priority: ").append(task.getPriority()).append("\n");
        prompt.append("Location: ").append(task.getLocation()).append("\n\n");

        prompt.append("### AVAILABLE PERSONNEL ###\n");
        for (int i = 0; i < persons.size(); i++) {
            PersonDto person = persons.get(i);
            prompt.append("Person ").append(i + 1).append(":\n");
            prompt.append("  ID: ").append(person.getId()).append("\n");
            prompt.append("  Name: ").append(person.getFirstName()).append(" ").append(person.getLastName()).append("\n");
            prompt.append("  Role: ").append(person.getRole()).append("\n");
            prompt.append("  Department: ").append(person.getDepartment()).append("\n");
            prompt.append("  Rank: ").append(person.getRank()).append("\n");
            prompt.append("  Organization: ").append(person.getOrganization()).append("\n");

            if (person.getSkills() != null && !person.getSkills().isEmpty()) {
                prompt.append("  Skills: ");
                prompt.append(person.getSkills().stream()
                        .map(skill -> skill.getSkillName() + " (Level: " + skill.getProficiencyLevel() + ")")
                        .collect(Collectors.joining(", ")));
                prompt.append("\n");
            }

            if (person.getMedicalCondition() != null) {
                prompt.append("  Medical Conditions: Has medical conditions\n");
            }

            prompt.append("  Status: ").append(person.getStatus()).append("\n");
            prompt.append("  Shift: ");
            if (person.getShiftStartTime() != null && person.getShiftEndTime() != null) {
                prompt.append(person.getShiftStartTime()).append(" to ").append(person.getShiftEndTime());
            } else {
                prompt.append("Not specified");
            }
            prompt.append("\n\n");
        }

        prompt.append("### INSTRUCTIONS ###\n");
        prompt.append("Based on the task requirements and personnel qualifications:\n");
        prompt.append("1. Consider the task type, priority, and location\n");
        prompt.append("2. Evaluate each person's role, skills, rank, and availability\n");
        prompt.append("3. Match skills and experience to task requirements\n");
        prompt.append("4. Consider medical conditions if relevant\n");
        prompt.append("5. Select the SINGLE most suitable person\n\n");

        prompt.append("RESPOND IN THIS EXACT JSON FORMAT (no additional text):\n");
        prompt.append("{\n");
        prompt.append("  \"personId\": <selected person ID as number>,\n");
        prompt.append("  \"reason\": \"<brief explanation of why this person is the best match>\",\n");
        prompt.append("  \"matchScore\": <confidence score between 0 and 100>\n");
        prompt.append("}\n");

        return prompt.toString();
    }

    /**
     * Calls the Gemini AI API with the prepared prompt and retrieves the response.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Constructs the API request body with prompt and generation config</li>
     *   <li>Sets appropriate headers for JSON communication</li>
     *   <li>Makes HTTP POST request to Gemini API endpoint</li>
     *   <li>Validates the response status and content</li>
     *   <li>Extracts text from the response structure</li>
     *   <li>Includes comprehensive debug logging</li>
     * </ol>
     *
     * <p><b>Configuration Parameters:</b></p>
     * <ul>
     *   <li>Temperature: 0.7 (balanced creativity and consistency)</li>
     *   <li>TopK: 40 (token sampling parameter)</li>
     *   <li>TopP: 0.95 (nucleus sampling parameter)</li>
     *   <li>MaxOutputTokens: 2048 (maximum response length)</li>
     *   <li>ResponseMimeType: application/json (forces JSON output)</li>
     * </ul>
     *
     * @param prompt the formatted prompt string for Gemini AI
     * @return the text response from Gemini AI
     * @throws RuntimeException if API call fails or returns empty response
     */
    private String callGeminiApi(String prompt) {
        try {
            System.out.println("=== DEBUG: Calling Gemini API ===");

            // Build request body for Gemini API
            Map<String, Object> requestBody = new HashMap<>();

            Map<String, Object> content = new HashMap<>();
            List<Map<String, String>> parts = new ArrayList<>();
            Map<String, String> part = new HashMap<>();
            part.put("text", prompt);
            parts.add(part);
            content.put("parts", parts);

            requestBody.put("contents", Collections.singletonList(content));

            // Add generation config for JSON output
            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("temperature", 0.7);
            generationConfig.put("topK", 40);
            generationConfig.put("topP", 0.95);
            generationConfig.put("maxOutputTokens", 2048);
            generationConfig.put("responseMimeType", "application/json");
            requestBody.put("generationConfig", generationConfig);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Build URL with API key
            String urlWithKey = geminiApiUrl + "?key=" + geminiApiKey;

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            System.out.println("API URL: " + geminiApiUrl);
            System.out.println("Request body prepared");

            // Call API
            ResponseEntity<String> response = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body Length: " + (response.getBody() != null ? response.getBody().length() : "null"));
            System.out.println("=== END DEBUG ===");

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                if (responseBody == null || responseBody.isEmpty()) {
                    throw new RuntimeException("Gemini API returned empty response");
                }
                return extractTextFromGeminiResponse(responseBody);
            } else {
                throw new RuntimeException("Gemini API call failed with status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            System.err.println("=== ERROR: Gemini API Call Failed ===");
            System.err.println("Exception: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            e.printStackTrace();
            System.err.println("=== END ERROR ===");
            throw new RuntimeException("Error calling Gemini API: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts text content from the Gemini API's structured JSON response.
     *
     * <p>This method navigates through the Gemini API response structure:</p>
     * <pre>
     * {
     *   "candidates": [
     *     {
     *       "content": {
     *         "parts": [
     *           {
     *             "text": "... extracted text ..."
     *           }
     *         ]
     *       }
     *     }
     *   ]
     * }
     * </pre>
     *
     * <p>Includes error handling for:</p>
     * <ul>
     *   <li>Missing 'candidates' field</li>
     *   <li>Empty candidates array</li>
     *   <li>Missing nested fields</li>
     *   <li>JSON parsing errors</li>
     * </ul>
     *
     * @param responseBody the raw JSON response body from Gemini API
     * @return the extracted text content from the response
     * @throws RuntimeException if extraction fails or response structure is invalid
     * @throws JsonProcessingException if JSON parsing fails
     */
    private String extractTextFromGeminiResponse(String responseBody) {
        try {
            System.out.println("=== DEBUG: Extracting text from Gemini response ===");
            System.out.println("Response body length: " + responseBody.length());

            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode candidates = root.path("candidates");

            if (candidates.isMissingNode()) {
                System.err.println("ERROR: 'candidates' field is missing from response");
                System.err.println("Response structure: " + root.toString());
                throw new RuntimeException("'candidates' field missing from Gemini response");
            }

            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode firstCandidate = candidates.get(0);
                JsonNode content = firstCandidate.path("content");
                JsonNode parts = content.path("parts");

                if (parts.isArray() && parts.size() > 0) {
                    JsonNode firstPart = parts.get(0);
                    String extractedText = firstPart.path("text").asText();

                    System.out.println("Successfully extracted text length: " + extractedText.length());
                    System.out.println("=== END DEBUG ===");

                    return extractedText;
                }
            }

            System.err.println("ERROR: Could not find text in expected path");
            System.err.println("Candidates size: " + (candidates.isArray() ? candidates.size() : "not an array"));
            throw new RuntimeException("Could not extract text from Gemini response structure");
        } catch (JsonProcessingException e) {
            System.err.println("ERROR: JSON parsing failed");
            System.err.println("First 500 chars of response: " +
                (responseBody.length() > 500 ? responseBody.substring(0, 500) : responseBody));
            throw new RuntimeException("Error parsing Gemini response: " + e.getMessage(), e);
        }
    }

    /**
     * Parses the Gemini AI response and creates a TaskAssignmentDto object.
     *
     * <p>This method performs the following operations:</p>
     * <ol>
     *   <li>Cleans the response by removing markdown formatting (```json, ```)</li>
     *   <li>Trims trailing text after the JSON closing brace</li>
     *   <li>Parses the JSON to extract personId, reason, and matchScore</li>
     *   <li>Finds the selected person from the available persons list</li>
     *   <li>Creates and returns a TaskAssignmentDto with all details</li>
     * </ol>
     *
     * <p><b>Expected JSON Format:</b></p>
     * <pre>
     * {
     *   "personId": 123,
     *   "reason": "Best match because...",
     *   "matchScore": 95.5
     * }
     * </pre>
     *
     * <p><b>Fallback Behavior:</b></p>
     * <p>If parsing fails for any reason (malformed JSON, missing fields, etc.),
     * the method automatically selects the first available person with a default
     * match score of 50.0 and includes the error message in the reason.</p>
     *
     * @param geminiResponse the raw text response from Gemini AI
     * @param availablePersons the list of available personnel to match against
     * @param task the task being assigned
     * @return a TaskAssignmentDto with assignment details, never null
     */
    private TaskAssignmentDto parseGeminiResponse(String geminiResponse, List<PersonDto> availablePersons, TaskDto task) {
        try {
            System.out.println("=== DEBUG: Raw Gemini Response ===");
            System.out.println(geminiResponse);
            System.out.println("=== END DEBUG ===");

            // Extract JSON from response (Gemini might include markdown formatting)
            String jsonString = geminiResponse.trim();

            // Remove markdown code blocks
            if (jsonString.contains("```json")) {
                int startIndex = jsonString.indexOf("```json") + 7;
                int endIndex = jsonString.indexOf("```", startIndex);
                if (endIndex > startIndex) {
                    jsonString = jsonString.substring(startIndex, endIndex);
                }
            } else if (jsonString.contains("```")) {
                int startIndex = jsonString.indexOf("```") + 3;
                int endIndex = jsonString.indexOf("```", startIndex);
                if (endIndex > startIndex) {
                    jsonString = jsonString.substring(startIndex, endIndex);
                }
            }

            jsonString = jsonString.trim();

            // Additional cleanup: remove any trailing text after the JSON object
            int lastBraceIndex = jsonString.lastIndexOf("}");
            if (lastBraceIndex > 0 && lastBraceIndex < jsonString.length() - 1) {
                jsonString = jsonString.substring(0, lastBraceIndex + 1);
            }

            System.out.println("=== DEBUG: Cleaned JSON String ===");
            System.out.println(jsonString);
            System.out.println("=== END DEBUG ===");

            JsonNode responseJson = objectMapper.readTree(jsonString);

            long personId = responseJson.path("personId").asLong();
            String reason = responseJson.path("reason").asText();
            double matchScore = responseJson.path("matchScore").asDouble();

            System.out.println("=== DEBUG: Parsed Values ===");
            System.out.println("Person ID: " + personId);
            System.out.println("Reason: " + reason);
            System.out.println("Match Score: " + matchScore);
            System.out.println("=== END DEBUG ===");

            // Find the selected person
            PersonDto selectedPerson = availablePersons.stream()
                    .filter(p -> p.getId() == personId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Selected person ID " + personId + " not found in available persons"));

            return new TaskAssignmentDto(selectedPerson, task, reason, matchScore);

        } catch (Exception e) {
            // Fallback: select first available person if parsing fails
            System.err.println("=== ERROR: Failed to parse Gemini response ===");
            System.err.println("Exception: " + e.getClass().getName());
            System.err.println("Message: " + e.getMessage());
            System.err.println("Raw response length: " + (geminiResponse != null ? geminiResponse.length() : "null"));
            e.printStackTrace();
            System.err.println("=== END ERROR ===");

            PersonDto fallbackPerson = availablePersons.get(0);
            return new TaskAssignmentDto(
                    fallbackPerson,
                    task,
                    "Selected by fallback mechanism due to parsing error: " + e.getMessage(),
                    50.0
            );
        }
    }

    /**
     * Converts a Person entity to a PersonDto data transfer object.
     *
     * <p>This method maps the following fields:</p>
     * <ul>
     *   <li>Basic info: id, personal code, names, contact details</li>
     *   <li>Organizational: role, department, organization, rank</li>
     *   <li>Status: status, shift times, created/updated timestamps</li>
     *   <li>State: disabled flag</li>
     * </ul>
     *
     * <p><b>Note:</b> This method does not map complex relationships like
     * skills, medical conditions, or emergency contacts. If these are needed
     * in the DTO, additional mapping logic should be added.</p>
     *
     * @param person the Person entity to convert
     * @return a PersonDto with mapped data
     */
    private PersonDto convertToPersonDto(Person person) {
        PersonDto dto = new PersonDto();
        dto.setId(person.getId());
        dto.setPersonalCode(person.getPersonalCode());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
        dto.setPhone(person.getPhone());
        dto.setEmail(person.getEmail());
        dto.setAddress(person.getAddress());
        dto.setRole(person.getRole());
        dto.setDepartment(person.getDepartment());
        dto.setOrganization(person.getOrganization());
        dto.setRank(person.getRank());
        dto.setStatus(person.getStatus());
        dto.setShiftStartTime(person.getShiftStartTime());
        dto.setShiftEndTime(person.getShiftEndTime());
        dto.setCreatedAt(person.getCreatedAt());
        dto.setUpdatedAt(person.getUpdatedAt());
        dto.setDisabled(person.isDisabled());

        // Note: Skills, MedicalCondition, and EmergencyContacts would need separate mapping
        // if you want to include them in the DTO

        return dto;
    }
}
