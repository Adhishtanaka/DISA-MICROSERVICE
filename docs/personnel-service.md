# Personnel Service

**Port:** 8084
**Database:** PostgreSQL (`personnel_service`)
**Purpose:** Manages disaster response personnel with full medical and professional profiles, and provides AI-powered task-to-person matching via Google Gemini.

---

## What It Does

Personnel-service is the most feature-rich service. It stores comprehensive profiles for each responder (skills, medical conditions, documents, emergency contacts) and uses Google's Gemini AI to intelligently match pending tasks to the most suitable available personnel.

---

## API Routes

All endpoints require a valid JWT token (`Authorization: Bearer <token>`).

### Person Management — `/api/personnel/person`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/personnel/person` | Get all persons |
| GET | `/api/personnel/person/{id}` | Get person by ID |
| POST | `/api/personnel/person` | Create persons (batch) |
| PUT | `/api/personnel/person` | Update persons (batch) |
| PATCH | `/api/personnel/person/{id}` | Soft delete (mark as disabled) |
| DELETE | `/api/personnel/person/{id}` | Hard delete |

### Task Assignment — `/api/personnel/assignments`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/personnel/assignments/available-persons` | List available personnel |
| GET | `/api/personnel/assignments/pending-tasks` | Fetch PENDING tasks from task-service |
| POST | `/api/personnel/assignments/match-task` | AI-match a single task to best person |
| POST | `/api/personnel/assignments/match-all-pending` | AI-match all pending tasks (batch) |

### Medical & Professional Sub-resources

All follow the same pattern: `GET /`, `GET /{id}`, `POST /` (batch create), `PUT /` (batch update), `PATCH /{id}` (soft delete), `DELETE /{id}` (hard delete).

| Base Path | Resource |
|-----------|----------|
| `/api/personnel/allergies` | Allergies |
| `/api/personnel/skills` | Skills |
| `/api/personnel/medications` | Medications |
| `/api/personnel/chronic-conditions` | Chronic conditions |
| `/api/personnel/medical-conditions` | Medical conditions |
| `/api/personnel/physical-limitations` | Physical limitations |
| `/api/personnel/injury-histories` | Injury history |
| `/api/personnel/documents` | Documents |
| `/api/personnel/emergency-contacts` | Emergency contacts |

---

## Authentication & Security

- **Mechanism:** JWT — same tokens issued by auth-service
- **Header:** `Authorization: Bearer <token>`
- **Filter:** `JwtAuthenticationFilter` (extends `OncePerRequestFilter`)
- **Session:** STATELESS
- **CSRF:** Disabled
- **JWT Secret:** `${JWT_SECRET}` env var (BASE64 decoded, HMAC-SHA)

**Public (no auth):**
- `/swagger-ui/**`, `/v3/api-docs/**`, `/swagger-ui.html`, `/actuator/**`

**All other endpoints:** require valid JWT

**CORS:**
- Origin: `http://localhost:5173`
- Methods: GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD
- Credentials: allowed
- Max age: 3600s

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Queues & Routing

| Queue | Routing Key | Direction | Description |
|-------|-------------|-----------|-------------|
| `personnel.task.assigned.queue` | `task.assigned` | Consumed | Receives task assignment events from task-service |
| `personnel.status.queue` | `personnel.status.changed` | Consumed | Handles personnel status change events |

### Published Events

| Routing Key | Trigger | Payload |
|-------------|---------|---------|
| `personnel.status.changed` | Person create/update/delete | `PersonnelStatusEvent` |
| `personnel.available` | Personnel becomes available | `PersonnelStatusEvent` |

### PersonnelStatusEvent
```
personnelId:   Long
personnelCode: String
fullName:      String
status:        String
role:          String
skills:        List<String>
isAvailable:   Boolean
notes:         String
```

### TaskAssignedEvent (consumed)
```
taskId:      String
assignedTo:  String
taskType:    String
priority:    String
location:    String
description: String
```

All messages use `Jackson2JsonMessageConverter` (JSON). All queues are durable.

---

## DTOs

### PersonDto
Full person record with nested skills, emergency contacts, and medical condition.

### TaskDto
```
id:          Long
taskCode:    String
type:        TaskTypeDto  (RESCUE_OPERATION | MEDICAL_AID | DEBRIS_REMOVAL)
title:       String
description: String
priority:    PriorityDto  (LOW | MEDIUM | HIGH | URGENT)
incidentId:  Long
assignedTo:  Long
location:    String
status:      TaskStatusDto (PENDING | IN_PROGRESS | COMPLETED)
createdAt:   LocalDateTime
completedAt: LocalDateTime
```

### TaskAssignmentDto
```
assignedPerson:  PersonDto
task:            TaskDto
assignmentReason: String   (Gemini AI reasoning)
matchScore:      Double    (0–100 confidence score)
```

### SkillDto
```
id, skillName, experienceYears, missionCount
proficiencyLevel, certifications (List<DocumentDto>)
isDisabled, createdAt, updatedAt
```

### MedicalConditionDto
```
id, bloodGroup, height, weight
allergies, chronicConditions, physicalLimitations
medicalDocuments, pastInjuries, medications
isDisabled, createdAt, updatedAt
```

---

## Entities

### Person
```
id, personalCode, firstName, lastName
phone, email, address
role, department, organization, rank
status: AVAILABLE | ON_DUTY | ON_LEAVE | UNAVAILABLE
shiftStartTime, shiftEndTime
medicalCondition (OneToOne)
skills (OneToMany)
emergencyContacts (OneToMany)
isDisabled (soft delete flag)
createdAt, updatedAt
```

### MedicalCondition
```
id, bloodGroup, height, weight
allergies (OneToMany)
chronicConditions (OneToMany)
physicalLimitations (OneToMany)
pastInjuries / InjuryHistory (OneToMany)
medications (OneToMany)
isDisabled, createdAt, updatedAt
```

### Skill
```
id, person (ManyToOne)
profession / skillName
experienceYears, missionCount
level / proficiencyLevel
isDisabled, createdAt, updatedAt
```

All entities support **soft delete** via `isDisabled` flag. PATCH endpoints soft-delete; DELETE hard-deletes.

---

## AI Task Assignment (Gemini)

**Service:** `AssignmentService`

**Process:**
1. Fetch all available personnel (status contains "available", not disabled)
2. Fetch pending tasks from task-service via REST
3. Build a structured prompt with task requirements and personnel skills/availability
4. Call Gemini AI API with the prompt (JSON response format)
5. Parse AI response: extract `personId`, `reason`, `matchScore`
6. Return `TaskAssignmentDto`
7. **Fallback:** If AI parsing fails, select first available person with score `50.0`

**Gemini Config:**
- Temperature: `0.7`
- TopK: `40`, TopP: `0.95`
- MaxOutputTokens: `2048`
- ResponseMimeType: `application/json`

---

## External Integrations

| Service | URL Env Var | Default | Purpose |
|---------|-------------|---------|---------|
| Task Service | `TASK_API_URL` | `http://localhost:8088/api/v1` | Fetch pending tasks |
| Gemini AI | `GEMINI_API_URL` | `https://generativelanguage.googleapis.com/...` | AI matching |

---

## Exception Handling

| Exception | HTTP Status |
|-----------|-------------|
| `ResourceNotFoundException` | 404 |
| `BadRequestException` | 400 |
| `DuplicateResourceException` | 400 |
| Generic | 500 |

Error response includes: `message`, `timestamp`, `status`, `details`.

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | — | `8084` |
| DB Host | `DB_HOST` | `localhost` |
| DB Port | — | `5432` |
| DB Name | `DB_NAME` | `personnel_service` |
| DB Username | `DB_USERNAME` | `postgres` |
| DB Password | `DB_PASSWORD` | `123` |
| JWT Secret | `JWT_SECRET` | (required) |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | `admin` |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | `admin123` |
| Task API URL | `TASK_API_URL` | `http://localhost:8088/api/v1` |
| Gemini API URL | `GEMINI_API_URL` | `https://generativelanguage.googleapis.com/...` |
| Gemini API Key | `GEMINI_API_KEY` | (required) |

Hibernate DDL: `update`
