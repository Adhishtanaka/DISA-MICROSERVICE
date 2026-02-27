export type IncidentStatus = "REPORTED" | "ACTIVE" | "RESOLVED";

export type IncidentType =
  | "EARTHQUAKE"
  | "FLOOD"
  | "FIRE"
  | "LANDSLIDE"
  | "TSUNAMI"
  | "CYCLONE"
  | "DROUGHT";

export type Severity = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";

export interface Incident {
  id: number;
  incidentCode: string;
  type: IncidentType;
  severity: Severity;
  status: IncidentStatus;
  description: string;
  latitude: number;
  longitude: number;
  address: string;
  reportedAt: string;
  updatedAt: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}