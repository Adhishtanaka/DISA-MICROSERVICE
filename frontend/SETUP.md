# Quick Start Guide - Assessment Service Frontend

## 🚀 Getting Started in 5 Minutes

### Step 1: Prerequisites ✅
Make sure you have:
- **Node.js 18+** - [Download](https://nodejs.org/)
- **Backend Service** - Running on `http://localhost:8087`

### Step 2: Install & Configure 📦

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Create .env file with backend URL
echo "VITE_API_URL=http://localhost:8087" > .env
```

### Step 3: Run Development Server 🎮

```bash
npm run dev
```

Open your browser: **http://localhost:5173**

## 📋 What You Can Do

### 1. Create Assessment
```
✓ Click "New Assessment" button
✓ Fill in damage details
✓ Select severity level
✓ Choose required actions
✓ Click "Create"
```

### 2. Upload Photos
```
✓ Open assessment in Draft mode
✓ Go to "Photos" tab
✓ Drag-drop or select image file
✓ Photos saved automatically
```

### 3. Complete Assessment
```
✓ Open assessment details
✓ Go to "Actions" tab
✓ Click "Complete Assessment"
✓ Event published to trigger task creation
```

### 4. View Assessments
```
✓ Filter by "All", "Draft", or "Completed"
✓ Click eye icon to view details
✓ Click edit icon to modify draft
✓ Click trash to delete
```

## 📊 Key Features

| Feature | Details |
|---------|---------|
| **Create Assessments** | Full form with validation |
| **Photo Upload** | Drag-drop support, up to 10MB |
| **Real-time Sync** | Updates reflected instantly |
| **Search & Filter** | By status and incident |
| **Responsive Design** | Works on desktop and tablet |
| **Error Handling** | Friendly error messages |

## 🔧 Available Commands

```bash
# Development server
npm run dev

# Production build
npm run build

# Preview production build
npm run preview

# Lint code
npm run lint
```

## 🌐 API Endpoints Used

The frontend calls these backend endpoints:

- `POST /api/assessments` - Create
- `GET /api/assessments` - Get all
- `GET /api/assessments/{id}` - View one
- `PUT /api/assessments/{id}` - Update
- `PUT /api/assessments/{id}/complete` - Complete
- `POST /api/assessments/{id}/photos` - Upload photo
- `DELETE /api/assessments/{id}` - Delete

## 🐛 Troubleshooting

### "Failed to fetch assessments"
- ✓ Backend running at port 8087?
- ✓ `.env` has correct `VITE_API_URL`?
- ✓ No firewall blocking connection?

### "Port 5173 already in use"
```bash
npm run dev -- --port 3000
```

### TypeScript Errors
```bash
# Clear cache and reinstall
rm -rf node_modules dist
npm install
npm run build
```

## 📁 Project Structure
