# 🔍 Debugging & Troubleshooting Guide

## Quick Diagnostics

### 1. Check API Health (Quickest)
```javascript
debugAPI()
```

**Output Example:**
```
╔═══════════════════════════════════════╗
║      API Diagnostics for Frontend      ║
╚═══════════════════════════════════════╝

✅ Backend Health: HEALTHY
   API URL: http://localhost:8087
   Using Mock Data: false
   Mock Assessments Count: 2

🔑 Authentication:
   JWT Token: Present
   
📊 API Test Result:
   Test Call: GET /api/assessments
   Status: 200
   Response Time: 45ms
   Success: true

✅ Frontend Ready: Production mode
```

---

### 2. Enable Detailed Logging
```javascript
enableDetailedLogging()
```

**Output:**
```
🔧 Detailed logging enabled!
   📝 CRUD operations will be logged with colors
   📊 Performance metrics will be shown
   🎯 API responses will be tracked
```

---

## Common Issues & Fixes

### Issue 1: "Failed to fetch assessments"

**Error Message:**
```
⚠️ Error: Failed to fetch assessments
💡 Tip: Make sure the backend Assessment Service is running on port 8087
```

**Root Causes:**

#### A. Backend Not Running
```bash
# Check if backend is running
netstat -ano | findstr :8087

# If not running, backend server needs to be started
# Usually: java -jar backend.jar
# Or: npm start (from backend folder)
```

**Fix:**
```javascript
// In browser console
await getBackendHealth()
// Should show: { isHealthy: true, useMockData: false }

// If useMockData is true, backend is down and using mock data
```

#### B. Wrong API URL
```javascript
// Check configured API URL
console.log('Configured URL:', import.meta.env.VITE_API_URL)
// Should output: http://localhost:8087

// Set custom URL if needed
// Edit .env file: VITE_API_URL=http://localhost:8087
```

#### C. Port Conflict
```bash
# Port 8087 might be in use by another app
# Find what's using port 8087:
netstat -ano | findstr :8087

# Kill the process if needed (replace PID):
taskkill /PID <PID> /F
```

#### D. CORS Issues
**Error:** "Access to XMLHttpRequest has been blocked by CORS policy"

**Fix:** Backend must have CORS enabled
```java
// Backend needs:
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
```

**Verify CORS Headers:**
```javascript
// In DevTools Network tab
// Look for these headers in response:
// Access-Control-Allow-Origin: *
// Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
// Access-Control-Allow-Headers: Content-Type, Authorization
```

---

### Issue 2: "Cannot read property 'forEach' of undefined"

**Error:** Photo list breaks when `photoUrls` is undefined

**Root Cause:** Assessment has no photos yet

**Fix in Code:**
```typescript
// ❌ Wrong
assessment.photoUrls.forEach(...)

// ✅ Correct
assessment.photoUrls?.forEach(...)
// or
if (assessment.photoUrls?.length > 0) {
  assessment.photoUrls.forEach(...)
}
```

---

### Issue 3: "Not authorized" or "401 Unauthorized"

**Error Message:**
```
❌ Error: Not authorized
   Status: 401
```

**Root Causes:**

#### A. Invalid JWT Token
```javascript
// Check JWT token
const token = localStorage.getItem('token')
console.log('JWT Token:', token)

// Decode to check expiry
function decodeJWT(token) {
  const base64Url = token.split('.')[1]
  const jsonPayload = decodeURIComponent(
    atob(base64Url).split('').map((c) => 
      '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
    ).join('')
  )
  return JSON.parse(jsonPayload)
}

const payload = decodeJWT(token)
console.log('Token expires:', new Date(payload.exp * 1000))
```

#### B. Token Expired
**Solution:** Clear cached token and re-login
```javascript
localStorage.removeItem('token')
localStorage.removeItem('jwt')
// Page will auto-redirect to login
```

#### C. Backend Token Validation Failed
**Check:** Backend might have different secret key
```javascript
// Console test
const response = await fetch('http://localhost:8087/api/assessments', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
console.log('Status:', response.status)
```

---

### Issue 4: "TypeError: Cannot destructure property..."

**Error:**
```
TypeError: Cannot destructure property 'data' of undefined
```

**Root Cause:** API response is undefined

**Solution:**
```typescript
// ❌ Wrong - no null check
const { data } = response

// ✅ Correct - safe destructuring
const response = await assessmentAPI.getAssessmentById(id)
const data = response?.data || {}
console.log(data)
```

---

### Issue 5: Form Submission Hangs

**Symptoms:**
- Form submit button stays loading
- No error message shown
- App becomes unresponsive

**Debug Steps:**

1. **Check Console for Errors:**
```javascript
// Open DevTools Console
// Look for red error messages
// If there's a ❌ icon, click to see stack trace
```

2. **Enable Request Logging:**
```javascript
// In api.ts, look for [API] logs in console
// Shows which requests are pending

// Example:
// [API] POST http://localhost:8087/api/assessments
// (stays hanging if server doesn't respond)
```

3. **Check Network Tab:**
```
DevTools → Network
- Look for red "failed" requests
- Check if request shows as "pending"
- Note HTTP status code
```

4. **Check Request Timeout:**
```javascript
// Frontend waits 30 seconds for response
// If backend takes longer, request times out
// Increase timeout in src/lib/api.ts:

axiosInstance.defaults.timeout = 60000 // 60 seconds
```

5. **Test Direct Backend Call:**
```bash
# From terminal/cmd
curl -X GET http://localhost:8087/api/assessments -H "Content-Type: application/json"

# If this times out, backend is not responding
```

---

## Network Debugging

### Check API Endpoints

```javascript
// Test each endpoint manually
async function testAllEndpoints() {
  const endpoints = [
    { method: 'GET', url: '/assessments', label: 'Get All' },
    { method: 'POST', url: '/assessments', label: 'Create' },
    { method: 'GET', url: '/assessments/1', label: 'Get One' },
    { method: 'GET', url: '/assessments/completed', label: 'Get Completed' },
  ]
  
  for (const ep of endpoints) {
    try {
      const res = await fetch(`http://localhost:8087/api${ep.url}`, {
        method: ep.method,
        headers: { 'Content-Type': 'application/json' }
      })
      console.log(`✅ ${ep.label}: ${res.status}`)
    } catch (err) {
      console.error(`❌ ${ep.label}: ${err.message}`)
    }
  }
}

testAllEndpoints()
```

### Monitor Network Traffic

**DevTools → Network Tab:**

1. Open http://localhost:5173
2. Press F12 → Network tab
3. Trigger an action (create, read, update, delete)
4. Look for requests:
   - Check Status (200 = success, 4xx = client error, 5xx = server error)
   - Check Response headers (look for CORS headers)
   - Check Response body (data returned)
   - Check Timing (how long request took)

**Example Successful Request:**
```
GET /api/assessments
Status: 200 OK
Response Headers:
  Content-Type: application/json
  Access-Control-Allow-Origin: *
Response Body:
  [
    {id: 1, assessmentCode: 'ASS-001', ...},
    {id: 2, assessmentCode: 'ASS-002', ...}
  ]
Time: 45ms
```

**Example Failed Request:**
```
GET /api/assessments
Status: 0 (Network Error)
Error: net::ERR_CONNECTION_REFUSED
Notes: Backend not running on port 8087
```

---

## Browser Console Utilities

### 1. Check Mock Data Status

```javascript
// See if using mock data
const health = await getBackendHealth()
console.log({
  backend_healthy: health.isHealthy,
  api_url: health.apiUrl,
  using_mock: health.useMockData,
  mock_count: health.mockDataCount,
  timeout: `${health.timeout}ms`
})
```

**Output:**
```javascript
{
  backend_healthy: false,
  api_url: 'http://localhost:8087',
  using_mock: true,
  mock_count: 2,
  timeout: '30000ms'
}
```

### 2. Log CRUD Operation

```javascript
// Log a CREATE operation
logCRUDOperation('CREATE', 'Assessment', {
  code: 'ASS-001',
  severity: 'HIGH'
})

// Output:
// 🟢 CREATE - Assessment
//   Data: {code: 'ASS-001', severity: 'HIGH'}
```

### 3. Test Authentication

```javascript
// Check if token exists
const token = localStorage.getItem('token')
console.log('Token exists:', !!token)
console.log('Token length:', token?.length)

// Check if token in localStorage
console.log('All localStorage:', localStorage)

// Decode token if exists
if (token) {
  const parts = token.split('.')
  console.log('Token parts:', parts.length) // Should be 3
}
```

### 4. Measure API Performance

```javascript
async function measurePerformance() {
  const operations = [
    { name: 'Get All', fn: () => assessmentAPI.getAllAssessments() },
    { name: 'Get One', fn: () => assessmentAPI.getAssessmentById(1) },
    { name: 'Get Completed', fn: () => assessmentAPI.getCompletedAssessments() },
  ]
  
  console.group('⏱️ API Performance')
  for (const op of operations) {
    const start = performance.now()
    try {
      await op.fn()
      const ms = performance.now() - start
      console.log(`✅ ${op.name}: ${ms.toFixed(0)}ms`)
    } catch (err) {
      console.error(`❌ ${op.name}: Failed`)
    }
  }
  console.groupEnd()
}

measurePerformance()
```

---

## Log Analysis

### Understanding Console Output

**[API] Tag:**
```javascript
// [API] GET http://localhost:8087/api/assessments
// This is API REQUEST logging
```

**[API Response] Tag:**
```javascript
// [API] Response 200 {items: [...]}
// This is API RESPONSE logging
```

**[API Request Error] Tag:**
```javascript
// [API Request Error] Network timeout
// This is ERROR logging
```

**CRUD Operation Tag:**
```javascript
// 🟢 CREATE - Assessment
// 🔵 READ - Assessment
// 🟡 UPDATE - Assessment
// 🔴 DELETE - Assessment
// This shows CRUD operations
```

### Example Log Session

```
[APP LOAD]
✅ Backend health check passed
✅ Using real API (not mock data)
🔧 Detailed logging enabled in development mode

[USER ACTION: Click "New Assessment"]
🟢 CREATE - Assessment
📝 Data: {assessmentCode: 'NEW-001', severity: 'HIGH', ...}
[API] POST http://localhost:8087/api/assessments
📤 Request Headers: {Authorization: 'Bearer ...', Content-Type: 'application/json'}

[API RESPONSE]
[API] Response 200
✅ Created successfully: ID 123
📥 Response Body: {id: 123, assessmentCode: 'NEW-001', status: 'DRAFT', ...}
⏱️ Response time: 234ms

[UI UPDATE]
✅ Assessment added to dashboard
✅ Form cleared
✅ Success notification shown
```

---

## TypeScript Errors

### Common Compilation Errors

**Error: "Property 'X' does not exist on type 'Y'"**
```typescript
// ❌ Wrong - missing optional chaining
const url = assessment.photoUrls[0]

// ✅ Correct
const url = assessment.photoUrls?.[0]

// ✅ Also correct
if (assessment.photoUrls && assessment.photoUrls.length > 0) {
  const url = assessment.photoUrls[0]
}
```

**Error: "Cannot find module 'X'"**
```typescript
// ❌ Wrong - incorrect path
import { api } from 'lib/api'

// ✅ Correct
import { assessmentAPI } from '@/lib/api'
```

**Error: "Argument of type 'X' is not assignable to parameter of type 'Y'"**
```typescript
// ❌ Wrong - type mismatch
assessmentAPI.createAssessment({
  estimatedCasualties: "5" // string instead of number
})

// ✅ Correct
assessmentAPI.createAssessment({
  estimatedCasualties: 5 // number
})
```

**Check All Errors:**
```bash
# From terminal
npm run build

# Shows all TypeScript errors before deployment
# Fix errors before pushing to production
```

---

## Performance Issues

### Slow Page Load

```javascript
// Measure component render time
console.time('Dashboard Render')
// Wait for Dashboard to render
console.timeEnd('Dashboard Render')
// Output: Dashboard Render: 234ms
```

**If slow:**
1. Check Network tab for slow requests
2. Look for multiple simultaneous requests
3. Check if mock data being used (slower in some cases)
4. Check browser console for warnings

### Memory Leaks

```javascript
// Check if memory usage increasing
console.memory
// { usedJSHeapSize, totalJSHeapSize, jsHeapSizeLimit }

// Monitor over time
setInterval(() => {
  const memory = performance.memory
  console.log(`Memory: ${(memory.usedJSHeapSize / 1048576).toFixed(2)} MB`)
}, 1000)

// Kill after testing: Ctrl+C in console
```

### Too Many Requests

```javascript
// Check if same request being made multiple times
// Look in Network tab or console logs
// If you see [API] GET /assessments multiple times quickly

// Solution 1: Add request deduplication
// Solution 2: Add request caching
// Solution 3: Use loading state to prevent duplicate submissions
```

---

## Error Recovery

### Automatic Error Recovery

The frontend automatically:
- ✅ Retries failed requests (configurable)
- ✅ Falls back to mock data if backend unavailable
- ✅ Shows user-friendly error messages
- ✅ Provides recovery actions (Retry, Reload, etc.)

### Manual Error Recovery

```javascript
// If app breaks, try these in console:

// 1. Reload page
location.reload()

// 2. Clear cache
localStorage.clear()
sessionStorage.clear()
// Then reload

// 3. Check backend health
await getBackendHealth()

// 4. Force mock data mode
// Edit .env: VITE_API_URL=http://invalid:9999
// Then reload page - will use mock data

// 5. Check JWT token
console.log('Token:', localStorage.getItem('token'))

// 6. Reset to real API
// Edit .env: VITE_API_URL=http://localhost:8087
// Then reload page
```

---

## Support Checklist

When reporting issues, include:

- [ ] Browser: Chrome/Firefox/Safari/Edge
- [ ] OS: Windows/Mac/Linux
- [ ] Frontend running: `npm run dev` output
- [ ] Backend running: Check port 8087
- [ ] Console errors: Screenshot or console log
- [ ] Network requests: Any failures in DevTools Network tab
- [ ] Steps to reproduce: Clear steps showing the problem
- [ ] Expected vs actual: What should happen vs what does
- [ ] debugAPI() output: Result from `window.debugAPI()`

**Example Issue Report:**
```
Browser: Chrome 120 on Windows 10
Issue: "Failed to fetch assessments" error

Steps to reproduce:
1. Start frontend: npm run dev
2. Open http://localhost:5173
3. Dashboard tab shows error

Console errors:
- GET http://localhost:8087/api/assessments (ERR_CONNECTION_REFUSED)

Network: All requests to localhost:8087 fail with error 0

debugAPI() output:
{
  isHealthy: false,
  apiUrl: 'http://localhost:8087',
  useMockData: true
}

Expected: Assessments should load and display
Actual: Error message shows

Attempt to fix: Verified backend not running on port 8087
```

---

**Effective Date:** 2024  
**Last Updated:** February 27, 2026  
**Maintenance:** Update when new issues discovered
