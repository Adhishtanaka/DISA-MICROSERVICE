# ─── Stage 1: Build ────────────────────────────────────────────────────────────
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files first for better layer caching
COPY package.json package-lock.json* ./

# Install ALL dependencies including devDependencies with BuildKit npm cache
RUN --mount=type=cache,target=/root/.npm \
    npm ci

# Copy all remaining files
COPY . .

# All API calls go through nginx (same origin), so bake in relative proxy paths.
# Override these build-args only if you know what you are doing.
ARG VITE_AUTH_SERVICE_URL=/api/auth
ARG VITE_INCIDENT_SERVICE_URL=/
ARG VITE_MISSION_SERVICE_URL=/
ARG VITE_RESOURCE_SERVICE_URL=/
ARG VITE_SHELTER_SERVICE_URL=/
ARG VITE_ASSESSMENT_SERVICE_URL=/
ARG VITE_TASK_SERVICE_URL=/api/v1/tasks

ENV VITE_AUTH_SERVICE_URL=$VITE_AUTH_SERVICE_URL
ENV VITE_INCIDENT_SERVICE_URL=$VITE_INCIDENT_SERVICE_URL
ENV VITE_MISSION_SERVICE_URL=$VITE_MISSION_SERVICE_URL
ENV VITE_RESOURCE_SERVICE_URL=$VITE_RESOURCE_SERVICE_URL
ENV VITE_SHELTER_SERVICE_URL=$VITE_SHELTER_SERVICE_URL
ENV VITE_ASSESSMENT_SERVICE_URL=$VITE_ASSESSMENT_SERVICE_URL
ENV VITE_TASK_SERVICE_URL=$VITE_TASK_SERVICE_URL

RUN npm run build

# ─── Stage 2: Runtime (Nginx) ──────────────────────────────────────────────────
FROM nginx:alpine

# Copy built files
COPY --from=builder /app/dist /usr/share/nginx/html

# Use nginx template — service URLs are substituted at container start via env vars.
# Defaults in docker-compose.yml point to Docker network service names.
COPY nginx.conf.template /etc/nginx/templates/default.conf.template

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
