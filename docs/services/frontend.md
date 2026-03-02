# Frontend — Deployment Guide

**Port:** 80
**Tech:** React 19 + Vite + Nginx reverse proxy

---

## What it does

Serves the React single-page application and proxies all API calls to the backend services via Nginx. The browser always communicates with the frontend's nginx server; nginx forwards requests to the appropriate backend.

---

## Deploy on a separate server

### 1. Deploy full stack (recommended)

The frontend `docker-compose.yml` starts everything — all 8 backend services, 8 databases, 1 RabbitMQ, and the frontend:

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/frontend

docker compose up --build -d
```

Access the app at `http://YOUR_SERVER`

### 2. Deploy frontend only (backends on separate servers)

The Nginx config uses environment variable substitution at container start. Set each service URL to your team member's server IP:

```bash
cd DISA-MICROSERVICE/frontend

AUTH_SERVICE_URL=http://192.168.1.11:8081 \
INCIDENT_SERVICE_URL=http://192.168.1.12:8083 \
MISSION_SERVICE_URL=http://192.168.1.13:8086 \
RESOURCE_SERVICE_URL=http://192.168.1.14:8089 \
SHELTER_SERVICE_URL=http://192.168.1.15:8085 \
ASSESSMENT_SERVICE_URL=http://192.168.1.16:8087 \
TASK_SERVICE_URL=http://192.168.1.17:8088 \
docker compose up --build -d frontend
```

Or set them in `frontend/docker-compose.yml` under the `frontend` service's `environment:` block.

---

## Nginx reverse proxy routes

| Frontend Path | Backend Service | Port |
|---|---|---|
| `/api/auth/*` | auth-service | 8081 |
| `/api/incidents*` | incident-service | 8083 |
| `/missions*` | mission-service | 8086 |
| `/api/resources*` | resource-service | 8089 |
| `/api/shelters*` | shelter-service | 8085 |
| `/api/assessments*` | assessment-service | 8087 |
| `/api/v1/*` | task-service | 8088 |

---

## Verify it's running

```bash
docker compose logs frontend --tail=20

# Open in browser
open http://localhost
```

## Available URLs (full stack)

| URL | What |
|---|---|
| `http://localhost` | React application |
| `http://localhost:8081/swagger-ui.html` | Auth Service API |
| `http://localhost:8083/swagger-ui.html` | Incident Service API |
| `http://localhost:8086/swagger-ui.html` | Mission Service API |
| `http://localhost:8089/swagger-ui.html` | Resource Service API |
| `http://localhost:8085/swagger-ui.html` | Shelter Service API |
| `http://localhost:8087/swagger-ui.html` | Assessment Service API |
| `http://localhost:8088/api/v1/swagger-ui.html` | Task Service API |
| `http://localhost:8084/swagger-ui.html` | Personnel Service API |
| `http://localhost:15672` | RabbitMQ Management (guest/guest) |
