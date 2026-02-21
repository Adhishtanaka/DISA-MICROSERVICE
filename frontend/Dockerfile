# ─── Stage 1: Build ────────────────────────────────────────────────────────────
FROM node:20-alpine AS builder

WORKDIR /app

# Install pnpm
RUN npm install -g pnpm

# Copy all files first
COPY . .

# Install ALL dependencies including devDependencies
RUN pnpm install

# Build with the API URL passed at build time
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL

RUN pnpm run build

# ─── Stage 2: Runtime (Nginx) ──────────────────────────────────────────────────
FROM nginx:alpine

# Copy built files
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
