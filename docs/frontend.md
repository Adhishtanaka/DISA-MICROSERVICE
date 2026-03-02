# Frontend

**Framework:** React 19 + TypeScript
**Build tool:** Vite
**Styling:** Tailwind CSS 4
**State management:** Zustand
**HTTP client:** Axios
**Routing:** React Router 7

---

## What It Does

The DISA frontend is a single-page disaster management coordination application. It provides a dashboard for managing missions, incidents, shelters, resources, assessments, and tasks during disaster/emergency operations. The app communicates with 7 backend microservices through Axios clients, all secured via JWT bearer tokens.

---

## Auth Handling

- **Token storage:** `localStorage` key `'auth_token'`
- **Auth state:** Zustand store (`authStore`) persisted to `localStorage` under `'disa-auth'`
- **Header injection:** Every Axios instance has a request interceptor that adds `Authorization: Bearer <token>` automatically
- **Login/Register:** On success, token is saved to `authStore` and `localStorage`
- **Logout:** Token removed from `localStorage`, auth state cleared

### Auth Store
```
token:           string | null
user:            UserProfile | null
isAuthenticated: boolean
```

### User Roles
`ADMIN` | `COORDINATOR` | `RESPONDER` | `VOLUNTEER`

---

## Route Structure

### Public Routes
| Path | Component |
|------|-----------|
| `/login` | LoginPage |
| `/register` | RegisterPage |

### Protected Routes (redirect to `/login` if unauthenticated)
| Path | Component |
|------|-----------|
| `/` | Redirect → `/missions` |
| `/missions` | MissionsPage |
| `/incidents` | IncidentsPage |
| `/assessments` | AssessmentsPage |
| `/shelters` | SheltersPage |
| `/tasks` | TasksPage |
| `*` | 404 NotFoundPage |

All protected routes are wrapped in a `Navigation` layout (top navbar with links, user info, logout).

---

## API Calls by Service

### Auth Service — `http://localhost:8081/api/auth`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `/register` | POST | `useRegister` hook |
| `/login` | POST | `useLogin` hook |
| `/validate` | GET | `authApi.validate` |
| `/profile` | GET | `useProfile` hook |
| `/profile` | PUT | `useProfile` hook |

---

### Mission Service — `http://localhost:8086`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `/missions` | GET | `useMissions` hook |
| `/missions/{id}` | GET | `missionApi.getById` |
| `/missions/status/{status}` | GET | `missionApi.getByStatus` |
| `/missions/type/{type}` | GET | `missionApi.getByType` |
| `/missions` | POST | `useCreateMission` hook |
| `/missions/{id}/status` | PUT | `useUpdateMissionStatus` hook |
| `/missions/{id}` | DELETE | `useDeleteMission` hook |

---

### Incident Service — `http://localhost:8083`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `/api/incidents?page={page}&size={size}` | GET | `useIncidents` hook (paginated) |
| `/api/incidents/{id}` | GET | `useIncident` hook |

---

### Assessment Service — `http://localhost:8087`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `/api/assessments` | GET | `useAssessments` hook |
| `/api/assessments/{id}` | GET | `assessmentApi.getById` |
| `/api/assessments/incident/{incidentId}` | GET | `useAssessmentsByIncident` hook |
| `/api/assessments/completed` | GET | `assessmentApi.getCompleted` |
| `/api/assessments` | POST | `assessmentApi.create` |
| `/api/assessments/{id}` | PUT | `assessmentApi.update` |
| `/api/assessments/{id}/complete` | PUT | `assessmentApi.complete` |
| `/api/assessments/{id}/photos` | POST | `assessmentApi.uploadPhoto` (multipart/form-data) |
| `/api/assessments/{id}` | DELETE | `assessmentApi.delete` |

---

### Shelter Service — `http://localhost:8085`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `/api/shelters` | GET | `useShelters` hook |
| `/api/shelters/{id}` | GET | `useShelter` hook |
| `/api/shelters/available` | GET | `useShelters(available=true)` |
| `/api/shelters/nearby?latitude=&longitude=&radiusKm=` | GET | `shelterApi.getNearby` |
| `/api/shelters` | POST | `shelterApi.create` |
| `/api/shelters/{id}` | PUT | `shelterApi.update` |
| `/api/shelters/{id}/checkin` | POST | `shelterApi.checkIn` |
| `/api/shelters/{id}/checkout` | POST | `shelterApi.checkOut` |
| `/api/shelters/{id}` | DELETE | `shelterApi.delete` |

---

### Task Service — `http://localhost:8088/api/v1/tasks`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `` | GET | `useTasks` hook |
| `/{id}` | GET | `taskApi.getById` |
| `` | POST | `taskApi.create` |
| `/{id}` | PUT | `taskApi.update` |
| `/{id}/assign` | PUT | `taskApi.assign` |
| `/{id}/complete` | PUT | `taskApi.complete` |
| `/{id}` | DELETE | `taskApi.remove` |

---

### Resource Service — `http://localhost:8089`

| Endpoint | Method | Used By |
|----------|--------|---------|
| `/api/resources` | GET | `useResources` hook |
| `/api/resources/{id}` | GET | `resourceApi.getById` |
| `/api/resources/type/{type}` | GET | `resourceApi.getByType` |
| `/api/resources/low-stock` | GET | `resourceApi.getLowStock` |
| `/api/resources` | POST | `useCreateResource` hook |
| `/api/resources/{id}` | PUT | `useUpdateResource` hook |
| `/api/resources/{id}/stock` | PUT | `resourceApi.setStock` |
| `/api/resources/{id}/increment` | PUT | `useStockOperation (INCREMENT)` |
| `/api/resources/{id}/decrement` | PUT | `useStockOperation (DECREMENT)` |
| `/api/resources/{id}` | DELETE | `useDeleteResource` hook |

---

## Features

### Missions (`/src/features/missions/`)
- View, create, update status, and delete missions
- Status: `PENDING` | `IN_PROGRESS` | `COMPLETED` | `CANCELLED`
- Type: `DELIVERY` | `RESCUE` | `EVACUATION` | `MEDICAL_TRANSPORT` | `ASSESMENT`
- Client-side filtering by status, type, and search term
- State: Zustand `missionStore`
- Components: MissionCard, MissionFilters, MissionList, MissionForm, MissionDetailPanel, UpdateStatusModal

### Incidents (`/src/features/incidents/`)
- View paginated incident list with details
- Status: `REPORTED` | `ACTIVE` | `RESOLVED`
- Type: `EARTHQUAKE` | `FLOOD` | `FIRE` | `LANDSLIDE` | `TSUNAMI` | `CYCLONE` | `DROUGHT`
- Severity: `LOW` | `MEDIUM` | `HIGH` | `CRITICAL`
- State: React `useState` + `useEffect`
- Components: IncidentList, IncidentCard, IncidentDetail

### Assessments (`/src/features/assessments/`)
- Create and manage damage assessment reports
- Photo upload support (multipart/form-data)
- Status: `DRAFT` | `COMPLETED`
- Severity: `MINOR` | `MODERATE` | `SEVERE` | `CRITICAL`
- State: React hooks
- Components: AssessmentCard, CreateAssessmentForm

### Shelters (`/src/features/shelters/`)
- Manage shelters with real-time occupancy
- Find nearby shelters by lat/lon radius
- Check-in / check-out people
- Status: `OPERATIONAL` | `FULL` | `CLOSED` | `UNDER_PREPARATION`
- State: React hooks
- Components: ShelterList, ShelterCard, ShelterDetail, ShelterForm

### Tasks (`/src/features/tasks/`)
- Create, assign, and complete operational tasks
- Type: `RESCUE_OPERATION` | `MEDICAL_AID` | `DEBRIS_REMOVAL`
- Priority: `LOW` | `MEDIUM` | `HIGH` | `URGENT`
- Status: `PENDING` | `IN_PROGRESS` | `COMPLETED`
- State: React hooks
- Components: TaskCard, CreateTaskForm

### Resources (`/src/features/resources/`)
- Manage disaster supply inventory
- Stock operations: set, increment, decrement
- Filter by type, low-stock flag, search
- Type: `FOOD` | `WATER` | `MEDICINE` | `EQUIPMENT` | `CLOTHING` | `HYGIENE`
- State: Zustand `resourceStore`
- Components: ResourceList, ResourceCard, ResourceDetailPanel, ResourceFilters, StockUpdateModal

### Auth (`/src/features/auth/`)
- Login, register, profile management
- JWT token lifecycle
- State: Zustand `authStore` (persisted to localStorage)
- Components: LoginForm, RegisterForm

---

## State Management

| Store | Engine | Persisted | Purpose |
|-------|--------|-----------|---------|
| `authStore` | Zustand | Yes (localStorage `'disa-auth'`) | JWT token, user profile, auth state |
| `missionStore` | Zustand | No | Missions list, filters, selected mission |
| `resourceStore` | Zustand | No | Resources list, filters, stock ops |
| Other features | React hooks | No | Local component state |

---

## Shared UI Components (`/src/components/ui/`)

`Badge` | `Button` | `Card` | `Input` | `Modal` | `Select` | `Spinner`

---

## Nginx Proxy (Docker)

In the Docker/production environment, Nginx proxies API requests to the appropriate backend service:

| Nginx path | Backend |
|------------|---------|
| `/api/auth/` | `auth-service:8081` |
| `/api/incidents` | `incident-service:8083` |
| `/missions` | `mission-service:8086` |
| `/api/resources` | `resource-service:8089` |
| `/api/shelters` | `shelter-service:8085` |
| `/api/assessments` | `assessment-service:8087` |
| `/api/v1/` | `task-service:8088` |
| `/` | React SPA (index.html fallback) |

Static assets cached for 1 year. Gzip compression enabled.

---

## Key Libraries

| Library | Version | Purpose |
|---------|---------|---------|
| React | 19.2.0 | UI framework |
| TypeScript | 5.9.3 | Type safety |
| React Router | 7.9.6 | Client-side routing |
| Zustand | 5.0.8 | State management |
| Axios | 1.13.4 | HTTP client |
| Tailwind CSS | 4.1.18 | Styling |
| Lucide React | 0.563.0 | Icons |
| jwt-decode | 4.0.0 | JWT parsing |
| Vite | 7.2.4 | Build tool |
