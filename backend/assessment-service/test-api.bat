@echo off
REM Assessment Service API Testing Script

echo.
echo ======================================
echo Assessment Service API Tests
echo ======================================
echo.

REM Wait for service to be ready
timeout /t 2 /nobreak

REM Test 1: Create an Assessment
echo [1] Creating an Assessment...
curl -X POST http://localhost:8087/api/assessments ^
  -H "Content-Type: application/json" ^
  -d "{\"incidentId\": 1, \"assessorId\": 301, \"assessorName\": \"John Assessor\", \"severity\": \"CRITICAL\", \"findings\": \"Severe structural damage\", \"requiredActions\": [\"RESCUE\", \"MEDICAL_AID\"], \"estimatedCasualties\": 10, \"estimatedDisplaced\": 50, \"status\": \"DRAFT\"}"

echo.
echo.

REM Test 2: Get all Assessments
echo [2] Getting all Assessments...
curl -X GET http://localhost:8087/api/assessments

echo.
echo.

REM Test 3: Get Assessment by ID (ID=1 if created successfully)
echo [3] Getting Assessment by ID (1)...
curl -X GET http://localhost:8087/api/assessments/1

echo.
echo.

REM Test 4: Get Assessments by Incident ID
echo [4] Getting Assessments for Incident ID 1...
curl -X GET http://localhost:8087/api/assessments/incident/1

echo.
echo.

REM Test 5: Update Assessment
echo [5] Updating Assessment 1...
curl -X PUT http://localhost:8087/api/assessments/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"incidentId\": 1, \"assessorId\": 301, \"assessorName\": \"John Assessor Updated\", \"severity\": \"SEVERE\", \"findings\": \"Updated findings\", \"requiredActions\": [\"RESCUE\"], \"estimatedCasualties\": 15, \"estimatedDisplaced\": 60, \"status\": \"DRAFT\"}"

echo.
echo.

REM Test 6: Complete Assessment
echo [6] Completing Assessment 1...
curl -X PUT http://localhost:8087/api/assessments/1/complete

echo.
echo.

REM Test 7: Get Completed Assessments
echo [7] Getting Completed Assessments...
curl -X GET http://localhost:8087/api/assessments/status/completed

echo.
echo.

echo ======================================
echo Tests Complete
echo ======================================
