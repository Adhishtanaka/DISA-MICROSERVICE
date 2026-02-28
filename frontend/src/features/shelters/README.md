# Shelter Feature

Frontend UI for the Shelter Service - manages evacuation shelters and tracks occupancy.

## Features

- 📋 View all shelters with status and capacity
- ➕ Create new shelters
- 👥 Check-in/check-out evacuees
- 📊 Real-time occupancy tracking
- 🔍 Filter available shelters
- 📍 Location-based shelter information
- 🏥 Facility information display

## Components

### Pages
- `SheltersPage` - Main page with shelter list and statistics

### Components
- `ShelterCard` - Display shelter summary with occupancy bar
- `ShelterList` - Grid layout of shelter cards
- `ShelterDetail` - Modal with full shelter details and check-in/out
- `ShelterForm` - Modal form to create new shelters

### Hooks
- `useShelters(availableOnly?)` - Fetch all or available shelters
- `useShelter(id)` - Fetch single shelter by ID

### API
- `shelterApi` - All API calls to shelter service

## Usage

```tsx
import { SheltersPage } from '@/features/shelters';

// In your router
<Route path="/shelters" element={<SheltersPage />} />
```

## Environment Variables

Add to your `.env` file:

```
VITE_SHELTER_SERVICE_URL=http://localhost:8085/api/shelters
```

## Shelter Status Types

- `OPERATIONAL` - Open and accepting evacuees (green)
- `FULL` - At capacity (red)
- `UNDER_PREPARATION` - Being prepared for use (yellow)
- `CLOSED` - Not accepting evacuees (gray)

## API Endpoints Used

- `GET /api/shelters` - Get all shelters
- `GET /api/shelters/{id}` - Get shelter by ID
- `GET /api/shelters/available` - Get available shelters
- `GET /api/shelters/nearby?latitude=X&longitude=Y&radiusKm=Z` - Get nearby shelters
- `POST /api/shelters` - Create shelter
- `PUT /api/shelters/{id}` - Update shelter
- `POST /api/shelters/{id}/checkin` - Check-in evacuees
- `POST /api/shelters/{id}/checkout` - Check-out evacuees
- `DELETE /api/shelters/{id}` - Delete shelter

## Features in Detail

### Statistics Dashboard
Shows real-time stats:
- Total shelters
- Operational shelters
- Full shelters
- Total capacity across all shelters
- Current total occupancy

### Occupancy Tracking
- Visual progress bar showing occupancy percentage
- Color-coded based on capacity:
  - Green: < 70% full
  - Yellow: 70-90% full
  - Red: > 90% full

### Check-in/Check-out
- Only available for OPERATIONAL shelters
- Validates capacity before check-in
- Validates current occupancy before check-out
- Real-time status updates

## Testing

```bash
# Start the shelter service
cd backend/shelter-service
mvn spring-boot:run

# Start the frontend
cd frontend
npm run dev

# Navigate to http://localhost:5173/shelters
```

## Integration with Other Services

The shelter service listens to `incident.created` events from RabbitMQ and automatically prepares nearby shelters when incidents are reported.
