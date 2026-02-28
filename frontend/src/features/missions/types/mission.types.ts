// ─── Enums ────────────────────────────────────────────────────────────────────

export type MissionStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export type MissionType =
  | 'DELIVERY'
  | 'RESCUE'
  | 'EVACUATION'
  | 'MEDICAL_TRANSPORT'
  | 'ASSESMENT';

// ─── Domain Model ─────────────────────────────────────────────────────────────

export interface Mission {
  id: number;
  missionCode: string;
  type: MissionType;
  status: MissionStatus;
  origin: string;
  destination: string;
  description: string | null;
  cargoDetails: string | null;
  vehicleId: string | null;
  vehicleType: string | null;
  driverId: string | null;
  driverName: string | null;
  incidentId: number | null;
  resourceId: number | null;
  notes: string | null;
  createdAt: string;
  startedAt: string | null;
  completedAt: string | null;
}

// ─── Request DTOs ─────────────────────────────────────────────────────────────

export interface CreateMissionRequest {
  type: MissionType;
  origin: string;
  destination: string;
  description?: string;
  cargoDetails?: string;
  vehicleType?: string;
}

export interface UpdateStatusRequest {
  missionId: number;
  status: MissionStatus;
  remarks?: string;
}
