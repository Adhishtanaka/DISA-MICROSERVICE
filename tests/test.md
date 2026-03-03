# DISA API Tests

HTTP test files for every service. Use the REST Client extension in VS Code (or IntelliJ HTTP Client) to run them.

## Test Order

Run in this sequence since later services depend on data from earlier ones:

1. **[auth.http](auth.http)** — Register all 4 roles, log in, capture tokens
2. **[incidents.http](incidents.http)** — Report incidents (needs RESPONDER/COORDINATOR token)
3. **[missions.http](missions.http)** — Create & manage missions
4. **[assessments.http](assessments.http)** — Submit field assessments
5. **[shelters.http](shelters.http)** — Create shelters, test check-in/out
6. **[tasks.http](tasks.http)** — Create and complete tasks
7. **[resources.http](resources.http)** — Manage resource inventory
8. **[personnel.http](personnel.http)** — Manage responder profiles

## Setup

Each `.http` file has a `@baseUrl` variable at the top. Change the host if your services are on a remote server.

After running the login requests in `auth.http`, copy the token values into the `@token` variables in each service file, or use the `@adminToken` / `@coordinatorToken` / `@responderToken` / `@volunteerToken` variables defined in `auth.http` if your HTTP client supports shared variables.

## Role Coverage

Each file tests:
- ✅ Allowed operations per role
- ❌ Forbidden operations (expect `403 Forbidden`)
