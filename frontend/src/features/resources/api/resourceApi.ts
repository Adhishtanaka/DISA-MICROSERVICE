import { resourceHttp } from '../../../lib/api';
import type {
  Resource,
  ResourceType,
  CreateResourceRequest,
  UpdateResourceRequest,
  StockUpdateRequest,
} from '../types/resource.types';

const BASE = '/api/resources';

export const resourceApi = {
  getAll: (): Promise<Resource[]> =>
    resourceHttp.get<Resource[]>(BASE).then((r) => r.data),

  getById: (id: number): Promise<Resource> =>
    resourceHttp.get<Resource>(`${BASE}/${id}`).then((r) => r.data),

  getByType: (type: ResourceType): Promise<Resource[]> =>
    resourceHttp.get<Resource[]>(`${BASE}/type/${type}`).then((r) => r.data),

  getLowStock: (): Promise<Resource[]> =>
    resourceHttp.get<Resource[]>(`${BASE}/low-stock`).then((r) => r.data),

  create: (payload: CreateResourceRequest): Promise<Resource> =>
    resourceHttp.post<Resource>(BASE, payload).then((r) => r.data),

  update: (id: number, payload: UpdateResourceRequest): Promise<Resource> =>
    resourceHttp.put<Resource>(`${BASE}/${id}`, payload).then((r) => r.data),

  setStock: (id: number, payload: StockUpdateRequest): Promise<Resource> =>
    resourceHttp.put<Resource>(`${BASE}/${id}/stock`, payload).then((r) => r.data),

  incrementStock: (id: number, payload: StockUpdateRequest): Promise<Resource> =>
    resourceHttp.put<Resource>(`${BASE}/${id}/increment`, payload).then((r) => r.data),

  decrementStock: (id: number, payload: StockUpdateRequest): Promise<Resource> =>
    resourceHttp.put<Resource>(`${BASE}/${id}/decrement`, payload).then((r) => r.data),

  remove: (id: number): Promise<void> =>
    resourceHttp.delete(`${BASE}/${id}`).then(() => undefined),
};
