import api from '../../../lib/api';
import type {
  Mission,
  MissionStatus,
  MissionType,
  CreateMissionRequest,
  UpdateStatusRequest,
} from '../types/mission.types';

const BASE = '/missions';

export const missionApi = {
  /** Fetch all missions */
  getAll: (): Promise<Mission[]> =>
    api.get<Mission[]>(BASE).then((r) => r.data),

  /** Fetch a single mission by id */
  getById: (id: number): Promise<Mission> =>
    api.get<Mission>(`${BASE}/${id}`).then((r) => r.data),

  /** Fetch missions filtered by status */
  getByStatus: (status: MissionStatus): Promise<Mission[]> =>
    api.get<Mission[]>(`${BASE}/status/${status}`).then((r) => r.data),

  /** Fetch missions filtered by type */
  getByType: (type: MissionType): Promise<Mission[]> =>
    api.get<Mission[]>(`${BASE}/type/${type}`).then((r) => r.data),

  /** Create a new mission */
  create: (payload: CreateMissionRequest): Promise<Mission> =>
    api.post<Mission>(BASE, payload).then((r) => r.data),

  /** Update mission status */
  updateStatus: (id: number, payload: UpdateStatusRequest): Promise<Mission> =>
    api.put<Mission>(`${BASE}/${id}/status`, payload).then((r) => r.data),

  /** Delete a mission */
  remove: (id: number): Promise<void> =>
    api.delete(`${BASE}/${id}`).then(() => undefined),
};
