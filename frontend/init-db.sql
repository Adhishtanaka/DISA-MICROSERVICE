-- Creates one database per service so each service is fully isolated.
-- Executed once when the postgres container is first initialised.

CREATE DATABASE auth_db;
CREATE DATABASE incident_db;
CREATE DATABASE mission_db;
CREATE DATABASE resource_db;
CREATE DATABASE shelter_db;
CREATE DATABASE assessment_db;
CREATE DATABASE task_db;
