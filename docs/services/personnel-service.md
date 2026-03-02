# Personnel Service — Deployment Guide

**Port:** 8084
**Database:** PostgreSQL (`personnel_service`)
**RabbitMQ:** Required
**External API:** Gemini AI (optional, for AI-powered task matching)

---

## What it does

Manages responder profiles including skills, availability, and medical data. Integrates with Gemini AI to intelligently match responders to tasks based on required skills.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)
- *(Optional)* A Google Gemini API key for AI task matching

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/personnel-service

RABBITMQ_HOST=YOUR_RABBITMQ_IP \
GEMINI_API_KEY=your-real-gemini-key \
docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_HOST` | `postgres` | PostgreSQL hostname |
| `DB_PORT` | `5432` | PostgreSQL port |
| `DB_NAME` | `personnel_service` | Database name |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |
| `JWT_SECRET` | *(dev key)* | Must match auth-service JWT secret |
| `GEMINI_API_KEY` | `placeholder-...` | Google Gemini API key for AI matching |
| `TASK_API_URL` | `http://task-service:8088/api/v1` | URL of the task-service |

### 4. Connect to task-service

Personnel service calls task-service to retrieve available tasks for AI matching. Set `TASK_API_URL` to the URL of your task-service:

```bash
TASK_API_URL=http://YOUR_TASK_SERVER:8088/api/v1 docker compose up -d personnel-service
```

### 5. JWT Secret alignment

The `JWT_SECRET` must match the secret used by auth-service for token validation:

```bash
JWT_SECRET=your-shared-secret docker compose up -d personnel-service
```

---

## Verify it's running

```bash
docker compose logs personnel-service --tail=20

# Swagger UI
open http://YOUR_SERVER:8084/swagger-ui.html
```
