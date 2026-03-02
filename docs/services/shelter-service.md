# Shelter Service — Deployment Guide

**Port:** 8085
**Database:** PostgreSQL (`shelter_db`)
**RabbitMQ:** Required (subscribes to `incident.created`)

---

## What it does

Manages evacuation shelters. Subscribes to incident events to pre-position shelters automatically. Tracks capacity and handles resident check-in/check-out.

---

## Deploy on a separate server

### 1. Prerequisites

- A running **RabbitMQ** server — see [rabbitmq.md](../rabbitmq.md)

### 2. Start the service

```bash
git clone https://github.com/your-org/DISA-MICROSERVICE.git
cd DISA-MICROSERVICE/backend/shelter-service

RABBITMQ_HOST=YOUR_RABBITMQ_IP docker compose up --build -d
```

### 3. Environment variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://postgres:5432/shelter_db` | PostgreSQL connection URL |
| `DB_USERNAME` | `postgres` | Database username |
| `DB_PASSWORD` | `postgres` | Database password |
| `RABBITMQ_HOST` | `rabbitmq` | **Set this to your RabbitMQ server IP/hostname** |
| `RABBITMQ_PORT` | `5672` | RabbitMQ AMQP port |
| `RABBITMQ_USERNAME` | `guest` | RabbitMQ username |
| `RABBITMQ_PASSWORD` | `guest` | RabbitMQ password |

---

## Verify it's running

```bash
docker compose logs shelter-service --tail=20

# Swagger UI
open http://YOUR_SERVER:8085/swagger-ui.html
```

---

## Events consumed

| Queue | Routing Key | Action |
|---|---|---|
| `shelter.incident.created.queue` | `incident.created` | Promotes nearby shelters to OPERATIONAL status |
