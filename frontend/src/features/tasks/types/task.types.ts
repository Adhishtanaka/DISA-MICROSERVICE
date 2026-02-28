export type TaskType =
  | 'RESCUE_OPERATION'
  | 'MEDICAL_AID'
  | 'DEBRIS_REMOVAL'
  | 'SUPPLY_DELIVERY'
  | 'EVACUATION';

export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';

export type TaskStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';

export interface Task {
  id: number;
  taskCode: string;
  type: TaskType;
  title: string;
  description: string;
  priority: Priority;
  incidentId: number | null;
  assignedTo: number | null;
  location: string;
  status: TaskStatus;
  createdAt: string;
  completedAt: string | null;
}

export interface TaskRequest {
  type: TaskType;
  title: string;
  description: string;
  priority: Priority;
  incidentId?: number;
  location: string;
}

export interface AssignTaskRequest {
  assignedTo: number;
}
