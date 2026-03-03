# DISA — Role-Based Access Control

## Roles

| Role | Description |
|---|---|
| `ADMIN` | Full access to everything. Can delete any record. |
| `COORDINATOR` | Creates and manages missions, incidents, resources, shelters, tasks, and personnel. Cannot delete most records. |
| `RESPONDER` | Field operator. Reports incidents, submits assessments, completes tasks, checks in/out of shelters. Read-only on most data. |
| `VOLUNTEER` | Read-only access across all services. Can check in/out of shelters. |

---

## How It Works

1. On login/register, the auth-service issues a JWT containing `sub` (username) and `role` claim.
2. Each downstream service validates the token signature using the shared `JWT_SECRET` and extracts the `role` claim.
3. Spring Security maps the role to `ROLE_<ROLE>` authority (e.g. `ROLE_COORDINATOR`).
4. Controller endpoints are secured with `hasRole()` / `hasAnyRole()` rules.
5. The frontend reads the role from the login response, stores it in Zustand, and conditionally renders action buttons.

---

## Permission Matrix

### Incident Service (`POST 8083/api/incidents`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /api/incidents` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/incidents/{id}` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/incidents/status/{s}` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/incidents` | ✅ | ✅ | ✅ | ❌ |
| `PUT /api/incidents/{id}` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/incidents/{id}/escalate` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/incidents/{id}/status` | ✅ | ✅ | ❌ | ❌ |
| `DELETE /api/incidents/{id}` | ✅ | ❌ | ❌ | ❌ |

### Mission Service (`PORT 8086/missions`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /missions` | ✅ | ✅ | ✅ | ✅ |
| `GET /missions/{id}` | ✅ | ✅ | ✅ | ✅ |
| `POST /missions` | ✅ | ✅ | ❌ | ❌ |
| `PUT /missions/{id}/status` | ✅ | ✅ | ✅ | ❌ |
| `PUT /missions/{id}` | ✅ | ✅ | ❌ | ❌ |
| `DELETE /missions/{id}` | ✅ | ❌ | ❌ | ❌ |

### Assessment Service (`PORT 8087/api/assessments`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /api/assessments` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/assessments/{id}` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/assessments` | ✅ | ✅ | ✅ | ❌ |
| `PUT /api/assessments/{id}` | ✅ | ✅ | ✅ | ❌ |
| `PUT /api/assessments/{id}/complete` | ✅ | ✅ | ✅ | ❌ |
| `POST /api/assessments/{id}/photos` | ✅ | ✅ | ✅ | ❌ |
| `DELETE /api/assessments/{id}` | ✅ | ✅ | ❌ | ❌ |

### Shelter Service (`PORT 8085/api/shelters`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /api/shelters` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/shelters/{id}` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/shelters` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/shelters/{id}` | ✅ | ✅ | ❌ | ❌ |
| `POST /api/shelters/{id}/checkin` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/shelters/{id}/checkout` | ✅ | ✅ | ✅ | ✅ |
| `PUT /api/shelters/{id}/status` | ✅ | ✅ | ❌ | ❌ |
| `DELETE /api/shelters/{id}` | ✅ | ❌ | ❌ | ❌ |

### Task Service (`PORT 8088, context-path /api/v1`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /api/v1/tasks` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/v1/tasks/{id}` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/v1/tasks` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/v1/tasks/{id}` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/v1/tasks/{id}/assign` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/v1/tasks/{id}/complete` | ✅ | ✅ | ✅ | ❌ |
| `DELETE /api/v1/tasks/{id}` | ✅ | ❌ | ❌ | ❌ |

### Resource Service (`PORT 8089/api/resources`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /api/resources` | ✅ | ✅ | ✅ | ✅ |
| `GET /api/resources/{id}` | ✅ | ✅ | ✅ | ✅ |
| `POST /api/resources` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/resources/{id}` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/resources/{id}/stock` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/resources/{id}/increment` | ✅ | ✅ | ❌ | ❌ |
| `PUT /api/resources/{id}/decrement` | ✅ | ✅ | ✅ | ❌ |
| `DELETE /api/resources/{id}` | ✅ | ❌ | ❌ | ❌ |

### Personnel Service (`PORT 8084`)

| Endpoint | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| `GET /persons` | ✅ | ✅ | ✅ | ✅ |
| `GET /skills` | ✅ | ✅ | ✅ | ✅ |
| `GET /assignments` | ✅ | ✅ | ✅ | ✅ |
| `POST /persons` | ✅ | ✅ | ❌ | ❌ |
| `PUT /persons/{id}` | ✅ | ✅ | ❌ | ❌ |
| `DELETE /persons/{id}` | ✅ | ❌ | ❌ | ❌ |
| `POST /assignments` | ✅ | ✅ | ❌ | ❌ |
| `DELETE /assignments/{id}` | ✅ | ✅ | ❌ | ❌ |

---

## Frontend UI Rules

| UI Element | ADMIN | COORDINATOR | RESPONDER | VOLUNTEER |
|---|---|---|---|---|
| New Mission button | ✅ | ✅ | ❌ | ❌ |
| Mission: Update Status button | ✅ | ✅ | ✅ | ❌ |
| Mission: Delete button | ✅ | ❌ | ❌ | ❌ |
| New Task button | ✅ | ✅ | ❌ | ❌ |
| Task: Complete button | ✅ | ✅ | ✅ | ❌ |
| Task: Delete button | ✅ | ❌ | ❌ | ❌ |
| Create Assessment button | ✅ | ✅ | ✅ | ❌ |
| Assessment: Complete button | ✅ | ✅ | ✅ | ❌ |
| Assessment: Delete button | ✅ | ✅ | ❌ | ❌ |
| Create Shelter button | ✅ | ✅ | ❌ | ❌ |
| New Resource button | ✅ | ✅ | ❌ | ❌ |
| Resource: Stock/Edit buttons | ✅ | ✅ | ❌ | ❌ |
| Resource: Delete button | ✅ | ❌ | ❌ | ❌ |

---

## Creating Users for Each Role

```bash
BASE=http://localhost:8081/api/auth

# Admin
curl -X POST $BASE/register -H "Content-Type: application/json" \
  -d '{"username":"admin","email":"admin@disa.local","password":"Admin123!","fullName":"System Admin","role":"ADMIN"}'

# Coordinator
curl -X POST $BASE/register -H "Content-Type: application/json" \
  -d '{"username":"coord1","email":"coord1@disa.local","password":"Coord123!","fullName":"Jane Coordinator","role":"COORDINATOR"}'

# Responder
curl -X POST $BASE/register -H "Content-Type: application/json" \
  -d '{"username":"resp1","email":"resp1@disa.local","password":"Resp123!","fullName":"John Responder","role":"RESPONDER"}'

# Volunteer
curl -X POST $BASE/register -H "Content-Type: application/json" \
  -d '{"username":"vol1","email":"vol1@disa.local","password":"Vol123!","fullName":"Alice Volunteer","role":"VOLUNTEER"}'
```
