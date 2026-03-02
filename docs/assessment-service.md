# Assessment Service

**Port:** 8087
**Database:** PostgreSQL (`assessment_db`)
**Purpose:** Manages disaster damage assessments — creation, photo uploads, completion, and publishing completion events to downstream services.

---

## What It Does

Allows assessors to create damage assessment reports tied to incidents. Assessments go through a two-phase lifecycle: `DRAFT` (editable) → `COMPLETED` (locked). On completion, an event is published to RabbitMQ so other services (e.g., task-service) can auto-generate tasks based on required actions.

---

## API Routes

Base path: `/api/assessments`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/assessments` | Create a new assessment (status defaults to DRAFT) |
| GET | `/api/assessments` | Get all assessments |
| GET | `/api/assessments/{id}` | Get assessment by ID |
| GET | `/api/assessments/incident/{incidentId}` | Get assessments for a specific incident |
| GET | `/api/assessments/completed` | Get all completed assessments |
| PUT | `/api/assessments/{id}` | Update an assessment (only if DRAFT) |
| PUT | `/api/assessments/{id}/complete` | Complete assessment — locks it and publishes RabbitMQ event |
| POST | `/api/assessments/{id}/photos` | Upload a photo (multipart/form-data, max 10MB) |
| GET | `/api/assessments/photos/{filename}` | Download a photo (JPEG) |
| DELETE | `/api/assessments/{id}` | Delete an assessment |

---

## Authentication & Security

- **Auth:** None — all endpoints are publicly accessible (`permitAll()`)
- **CSRF:** Disabled
- **CORS origins:** `http://localhost:5173`, `http://localhost:3000`
- **Methods allowed:** GET, POST, PUT, DELETE, OPTIONS
- **Swagger:** Publicly accessible at `/swagger-ui.html` and `/api-docs`

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Published Events

| Trigger | Routing Key | Queue | Payload |
|---------|-------------|-------|---------|
| `PUT /assessments/{id}/complete` | `assessment.completed` | `assessment.completed.queue` | `AssessmentEvent` |

### AssessmentEvent Structure
```
eventType: "assessment.completed"
timestamp: LocalDateTime
payload:
  assessmentId:    Long
  assessmentCode:  String  (e.g., "ASS-001")
  incidentId:      Long
  severity:        String  (MINOR | MODERATE | SEVERE | CRITICAL)
  requiredActions: List<String>
  assessorId:      Long
  findings:        String
```

The task-service consumes this event and auto-generates tasks based on `requiredActions`.

**No events consumed** by this service.

---

## DTOs

### AssessmentRequest
```
incidentId:              Long          (required)
assessorId:              Long          (required)
assessorName:            String        (required)
severity:                DamageSeverity (required)
findings:                String        (optional, max 2000 chars)
recommendations:         String        (optional, max 1000 chars)
requiredActions:         List<String>  (optional)
estimatedCasualties:     Integer       (optional)
estimatedDisplaced:      Integer       (optional)
affectedInfrastructure:  String        (optional)
status:                  AssessmentStatus (optional, defaults to DRAFT)
```

### AssessmentResponse
```
id:                      Long
assessmentCode:          String
incidentId:              Long
assessorId:              Long
assessorName:            String
severity:                DamageSeverity
findings:                String
recommendations:         String
requiredActions:         List<String>
photoUrls:               List<String>
status:                  AssessmentStatus
estimatedCasualties:     Integer
estimatedDisplaced:      Integer
affectedInfrastructure:  String
createdAt:               LocalDateTime
completedAt:             LocalDateTime
```

---

## Entities

### Assessment
```
id:                     Long (PK, auto-generated)
assessmentCode:         String (auto-generated: ASS-001, ASS-002, ...)
incidentId:             Long
assessorId:             Long
assessorName:           String
severity:               DamageSeverity (stored as STRING)
findings:               String (max 2000 chars)
recommendations:        String (max 1000 chars)
requiredActions:        List<String> (element collection → assessment_required_actions table)
photoUrls:              List<String> (element collection → assessment_photos table)
status:                 AssessmentStatus (DRAFT | COMPLETED)
estimatedCasualties:    Integer
estimatedDisplaced:     Integer
affectedInfrastructure: String
createdAt:              LocalDateTime (auto via @CreationTimestamp)
completedAt:            LocalDateTime (set on completion)
```

### DamageSeverity Enum
- `MINOR` — Limited damage
- `MODERATE` — Significant damage
- `SEVERE` — Extensive damage
- `CRITICAL` — Catastrophic damage

### AssessmentStatus Enum
- `DRAFT` — In progress, editable
- `COMPLETED` — Finalized, locked

---

## Key Service Behaviors

- Assessment codes auto-generated as `ASS-001`, `ASS-002`, ... (based on count)
- Updates are blocked on `COMPLETED` assessments
- Completing an already-completed assessment throws an error
- `completedAt` is auto-set when completing
- Photo files stored with UUID-based names under `./uploads/`
- Photo paths with `..` are rejected (path traversal protection)

---

## Exception Handling

| Exception | HTTP Status |
|-----------|-------------|
| `RuntimeException` | 400 |
| `MethodArgumentNotValidException` | 400 (field-level errors) |
| Generic `Exception` | 500 |

All error responses include: `timestamp`, `message`, `status`.

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | — | `8087` |
| DB URL | `DB_URL` | `jdbc:postgresql://localhost:5432/assessment_db` |
| DB Username | `DB_USERNAME` | `postgres` |
| DB Password | `DB_PASSWORD` | `postgres` |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | `guest` |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | `guest` |
| Max upload size | — | `10MB` |

Hibernate DDL: `update`
