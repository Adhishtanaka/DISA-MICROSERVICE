# Shelter Service

**Port:** 8085
**Database:** PostgreSQL (`shelter_db`)
**Purpose:** Manages emergency shelters — registration, capacity tracking, check-in/check-out, geographic search, and automatic preparation when incidents occur nearby.

---

## What It Does

Shelter-service tracks evacuation shelters and their real-time occupancy. When the incident-service publishes an `incident.created` event, this service automatically finds shelters within 50 km and promotes their status (CLOSED → UNDER_PREPARATION → OPERATIONAL) to prepare for incoming evacuees.

---

## API Routes

Base path: `/api/shelters`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/shelters` | Create a new shelter |
| GET | `/api/shelters` | Get all shelters |
| GET | `/api/shelters/{id}` | Get shelter by ID |
| GET | `/api/shelters/available` | Get available shelters (OPERATIONAL or UNDER_PREPARATION) |
| GET | `/api/shelters/nearby` | Find shelters within radius (params: `latitude`, `longitude`, `radiusKm=50`) |
| PUT | `/api/shelters/{id}` | Update shelter details |
| POST | `/api/shelters/{id}/checkin` | Check in people (increases occupancy) |
| POST | `/api/shelters/{id}/checkout` | Check out people (decreases occupancy) |
| PUT | `/api/shelters/{id}/status` | Manually update shelter status (param: `status`) |
| DELETE | `/api/shelters/{id}` | Delete a shelter (204 No Content) |

---

## Authentication & Security

- **Auth:** Spring Security present but **open** — all requests permitted without authentication
- **CSRF:** Disabled
- **CORS origins:** `http://localhost:5173`, `http://localhost:3000`
- **Methods:** GET, POST, PUT, DELETE, OPTIONS, PATCH
- **Credentials:** allowed
- **Max age:** 3600 seconds

---

## RabbitMQ

**Exchange:** `disaster.topic.exchange` (TopicExchange)

### Consumed Events

| Queue | Routing Key | Source | Action |
|-------|-------------|--------|--------|
| `shelter.incident.created.queue` | `incident.created` | incident-service | Call `prepareNearbyShelters()` |

### IncidentEvent (consumed)
```
eventType: String
timestamp: LocalDateTime
payload:
  incidentId:   Long
  incidentCode: String
  type:         String
  severity:     String
  latitude:     Double
  longitude:    Double
  address:      String
  description:  String
```

**On receipt:** finds all shelters within 50 km and upgrades status:
- `CLOSED` → `UNDER_PREPARATION`
- `UNDER_PREPARATION` → `OPERATIONAL`

**No events published** by this service.

---

## DTOs

### ShelterRequest
```
name:             String        (required)
address:          String        (required)
latitude:         Double        (required)
longitude:        Double        (required)
totalCapacity:    Integer       (required, positive)
currentOccupancy: Integer       (optional, default 0)
status:           ShelterStatus (optional, default OPERATIONAL)
contactPerson:    String
contactNumber:    String
facilities:       String        (medical, food, water, sanitation, etc.)
```

### CheckInRequest / CheckOutRequest
```
numberOfPeople: Integer (required, positive)
```

---

## Entities

### Shelter
```
id:               Long          (PK, auto-generated)
shelterCode:      String        (unique, e.g., SHE-001, SHE-002, ...)
name:             String
address:          String
latitude:         Double
longitude:        Double
totalCapacity:    Integer
currentOccupancy: Integer
status:           ShelterStatus (stored as STRING)
contactPerson:    String
contactNumber:    String
facilities:       String        (max 1000 chars)
createdAt:        LocalDateTime (auto-managed)
updatedAt:        LocalDateTime (auto-managed)
```

**Helper methods on entity:**
- `getAvailableCapacity()` — `totalCapacity - currentOccupancy`
- `isFull()` — `currentOccupancy >= totalCapacity`
- `getOccupancyPercentage()` — percentage of capacity used

### ShelterStatus Enum
| Status | Meaning |
|--------|---------|
| `OPERATIONAL` | Open and accepting evacuees |
| `FULL` | At maximum capacity |
| `CLOSED` | Not accepting evacuees |
| `UNDER_PREPARATION` | Being prepared for use |

---

## Key Service Behaviors

- Shelter codes auto-generated as `SHE-001`, `SHE-002`, ... (count-based)
- **Check-in:** throws `RuntimeException` if insufficient capacity; auto-sets status to `FULL` when at capacity
- **Check-out:** auto-changes status back to `OPERATIONAL` when no longer full
- **Geographic search:** uses simplified Euclidean distance (111 km per degree), **not** Haversine — adequate for short ranges
- **prepareNearbyShelters():** called via RabbitMQ event; default radius = 50 km
- All mutations are `@Transactional`

### Repository Custom Queries
- `findByStatusIn(List<ShelterStatus>)` — find shelters matching any of the given statuses
- `findByShelterCode(String)` — find by code
- `findByStatus(ShelterStatus)` — find by single status

---

## Exception Handling

| Exception | HTTP Status |
|-----------|-------------|
| `RuntimeException` (e.g., not found, insufficient capacity) | 400 |
| `MethodArgumentNotValidException` | 400 (field-level) |
| Generic `Exception` | 500 |

All error responses include: `timestamp`, `status`, `error`, `message` or `fieldErrors`.

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | — | `8085` |
| DB URL | `DB_URL` | `jdbc:postgresql://localhost:5432/shelter_db` |
| DB Username | `DB_USERNAME` | `postgres` |
| DB Password | `DB_PASSWORD` | `postgres` |
| RabbitMQ Host | `RABBITMQ_HOST` | `localhost` |
| RabbitMQ Port | `RABBITMQ_PORT` | `5672` |
| RabbitMQ Username | `RABBITMQ_USERNAME` | `guest` |
| RabbitMQ Password | `RABBITMQ_PASSWORD` | `guest` |

Hibernate DDL: `update`
Swagger UI: `/swagger-ui.html` (title: "Shelter Service API", version: 1.0.0)
