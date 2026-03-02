# Auth Service

**Port:** 8081
**Database:** PostgreSQL (`auth_db`)
**Purpose:** Centralized JWT-based authentication and user account management for the entire DISA platform.

---

## What It Does

Handles user registration, login, JWT token issuance/validation, and profile management. It is the only service that issues JWT tokens — all other services validate requests against tokens issued here.

---

## API Routes

Base path: `/api/auth`

| Method | Path | Auth Required | Description |
|--------|------|---------------|-------------|
| POST | `/api/auth/register` | No | Register a new user account |
| POST | `/api/auth/login` | No | Authenticate and receive a JWT token |
| GET | `/api/auth/validate` | No | Validate a JWT token |
| GET | `/api/auth/profile` | Yes (JWT) | Get the authenticated user's profile |
| PUT | `/api/auth/profile` | Yes (JWT) | Update the authenticated user's profile |
| DELETE | `/api/auth/profile` | Yes (JWT) | Delete the authenticated user's account |

---

## Authentication & Security

- **Mechanism:** JWT via JJWT library (HS256 algorithm)
- **Token expiry:** 24 hours (86,400,000 ms)
- **Secret key:** Loaded from `JWT_SECRET` env var (default: dev key)
- **Session policy:** STATELESS — no server-side sessions
- **CSRF:** Disabled
- **CORS origins:** `http://localhost:5173`, `http://localhost:3000`
- **Password hashing:** BCryptPasswordEncoder

**JWT Filter flow:**
1. Client sends `Authorization: Bearer <token>` header
2. `JwtAuthenticationFilter` extracts and validates the token
3. If valid, sets the user in `SecurityContextHolder`
4. If invalid, request is rejected with 401

**Public endpoints:** `/register`, `/login`, `/validate`, Swagger UI paths
**Protected endpoints:** `/profile` (GET, PUT, DELETE)

---

## RabbitMQ

**None.** Auth-service does not use RabbitMQ. It is a purely synchronous REST service.

---

## DTOs

### LoginRequest
```
username: String (required)
password: String (required)
```

### RegisterRequest
```
username: String (required, 3–50 chars)
email:    String (required, valid email)
password: String (required, min 6 chars)
role:     Role   (optional, defaults to VOLUNTEER)
fullName: String (optional)
phoneNumber: String (optional)
```

### LoginResponse
```
token:    String
username: String
email:    String
role:     Role
fullName: String
```

### UpdateUserRequest
```
email:    String (required, valid email)
role:     Role   (optional)
fullName: String (optional)
phoneNumber: String (optional)
```

### UserProfileResponse
```
id:          Long
username:    String
email:       String
role:        Role
fullName:    String
phoneNumber: String
createdAt:   LocalDateTime
updatedAt:   LocalDateTime
```

---

## Entities

### User
```
id:          Long (PK, auto-generated)
username:    String (unique)
email:       String (unique)
password:    String (BCrypt hashed)
role:        Role (ADMIN | COORDINATOR | RESPONDER | VOLUNTEER)
fullName:    String
phoneNumber: String
createdAt:   LocalDateTime (auto-set)
updatedAt:   LocalDateTime (auto-updated)
```

### Role Enum
- `ADMIN`
- `COORDINATOR`
- `RESPONDER`
- `VOLUNTEER`

---

## Key Services

### AuthServiceImpl
- `register()` — checks for duplicate username/email, hashes password, generates JWT
- `login()` — uses `AuthenticationManager` to verify credentials, generates JWT
- `getProfile()` — fetches user from DB, returns profile DTO
- `updateUser()` — updates email, role, fullName, phoneNumber (with duplicate email check)
- `deleteUser()` — removes user from DB
- `validateToken()` — extracts username, loads user details, checks expiry

### JwtService
- `generateToken(UserDetails)` — creates signed JWT with expiry
- `extractUsername(token)` — parses subject claim
- `isTokenValid(token, UserDetails)` — validates expiry + username match
- `getSignInKey()` — decodes Base64 secret, returns HMAC-SHA key

---

## Exception Handling

| Exception | HTTP Status | Notes |
|-----------|-------------|-------|
| `RuntimeException` ("Username/Email already exists") | 400 | Duplicate registration |
| `MethodArgumentNotValidException` | 400 | Validation errors (field-level) |
| Generic `Exception` | 500 | Unexpected errors |

---

## Configuration

| Property | Env Var | Default |
|----------|---------|---------|
| Server port | `SERVER_PORT` | `8081` |
| DB URL | `DB_URL` | `jdbc:postgresql://localhost:5432/auth_db` |
| DB Username | `DB_USERNAME` | `postgres` |
| DB Password | `DB_PASSWORD` | `postgres` |
| JWT Secret | `JWT_SECRET` | `dev-secret-key-change-in-production-at-least-32-chars` |
| JWT Expiry | — | `86400000` ms (24 hours) |

Hibernate DDL: `update` (auto-creates/updates schema)
