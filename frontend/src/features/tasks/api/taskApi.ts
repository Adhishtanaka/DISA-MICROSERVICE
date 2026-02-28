import axios from 'axios';
import type { Task, TaskRequest, AssignTaskRequest } from '../types/task.types';

const api = axios.create({ baseURL: import.meta.env.VITE_TASK_SERVICE_URL });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const taskApi = {
  getAll: () =>
    api.get<Task[]>('').then((r) => r.data),

  getById: (id: number) =>
    api.get<Task>(`/${id}`).then((r) => r.data),

  create: (data: TaskRequest) =>
    api.post<Task>('', data).then((r) => r.data),

  update: (id: number, data: Partial<TaskRequest>) =>
    api.put<Task>(`/${id}`, data).then((r) => r.data),

  assign: (id: number, data: AssignTaskRequest) =>
    api.put<Task>(`/${id}/assign`, data).then((r) => r.data),

  complete: (id: number) =>
    api.put<Task>(`/${id}/complete`).then((r) => r.data),

  remove: (id: number) =>
    api.delete(`/${id}`).then((r) => r.data),
};
