# Auth Service — Deployment Guide

**Port:** 8081
**Database:** PostgreSQL (`auth_db`)
**RabbitMQ:** Not required

---

## What it does

Handles user registration, login, and JWT token issuance. All other services call `/api/auth/validate` to verify tokens. Supports roles: `ADMIN`, `COORDINATOR`, `RESPONDER`, `VOLUNTEER`.

---

## Deploy on a separate server

### 1. Clone the repo and build

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/auth-service
docker compose up --build -d
```

### 2. Environment variables

All variables have working defaults. Override only what you need:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/auth_db` | Full JDBC URL to your PostgreSQL instance |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `JWT_SECRET` | *(dev key)* | **Change in production** — at least 32 characters |

### 3. Connect to an external PostgreSQL

Override `DB_URL` to point to your database server:

```bash
# In docker-compose.yml environment section, or as env vars:
DB_URL=jdbc:postgresql://YOUR_DB_HOST:5432/auth_db \
DB_USERNAME=myuser \
DB_PASSWORD=mypassword \
JWT_SECRET=your-secret-at-least-32-characters \
docker compose up -d auth-service
```

Or remove the `postgres` service from `docker-compose.yml` and set `DB_URL` to your external DB.

---

## Verify it's running

```bash
# Check health
docker compose logs auth-service --tail=20

# Test registration
curl -X POST http://YOUR_SERVER:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","role":"ADMIN"}'

# Test login
curl -X POST http://YOUR_SERVER:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Swagger UI
open http://YOUR_SERVER:8081/swagger-ui.html
```

---

## Connecting other services

All other backend services must be able to reach `http://auth-service:8081/api/auth/validate` to verify JWT tokens. In a multi-server deployment, set the auth service URL in each service's configuration or update the JWT validation to point to `http://YOUR_AUTH_SERVER:8081`.
