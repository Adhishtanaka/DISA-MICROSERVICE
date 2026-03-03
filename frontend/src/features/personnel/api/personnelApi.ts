import axios from 'axios';
import type { Person, PersonRequest, TaskDto, TaskAssignment, AssignmentHistory } from '../types/personnel.types';

const personnelHttp = axios.create({
  baseURL: import.meta.env.VITE_PERSONNEL_SERVICE_URL || 'http://localhost:8084',
  headers: { 'Content-Type': 'application/json' },
  timeout: 15000,
});

// Longer timeout for Gemini AI endpoints (each call takes 3-10s, batch can take minutes)
const geminiHttp = axios.create({
  baseURL: import.meta.env.VITE_PERSONNEL_SERVICE_URL || 'http://localhost:8084',
  headers: { 'Content-Type': 'application/json' },
  timeout: 180000,
});

const addAuthInterceptor = (instance: ReturnType<typeof axios.create>) => {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem('auth_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
};

addAuthInterceptor(personnelHttp);
addAuthInterceptor(geminiHttp);

const PERSON_BASE = '/api/personnel/person';
const ASSIGNMENT_BASE = '/api/personnel/assignments';

export const personnelApi = {
  async getAll(): Promise<Person[]> {
    const response = await personnelHttp.get<Person[]>(PERSON_BASE);
    return response.data;
  },

  async getById(id: number): Promise<Person> {
    const response = await personnelHttp.get<Person>(`${PERSON_BASE}/${id}`);
    return response.data;
  },

  async create(data: PersonRequest): Promise<Person[]> {
    const response = await personnelHttp.post<Person[]>(PERSON_BASE, [data]);
    return response.data;
  },

  async update(data: PersonRequest): Promise<Person[]> {
    const response = await personnelHttp.put<Person[]>(PERSON_BASE, [data]);
    return response.data;
  },

  async softDelete(id: number): Promise<Person> {
    const response = await personnelHttp.patch<Person>(`${PERSON_BASE}/${id}`);
    return response.data;
  },

  async hardDelete(id: number): Promise<void> {
    await personnelHttp.delete(`${PERSON_BASE}/${id}`);
  },

  async getAvailablePersons(): Promise<Person[]> {
    const response = await personnelHttp.get<Person[]>(`${ASSIGNMENT_BASE}/available-persons`);
    return response.data;
  },

  async getPendingTasks(): Promise<TaskDto[]> {
    const response = await personnelHttp.get<TaskDto[]>(`${ASSIGNMENT_BASE}/pending-tasks`);
    return response.data;
  },

  async matchTask(task: TaskDto): Promise<TaskAssignment> {
    const response = await geminiHttp.post<TaskAssignment>(`${ASSIGNMENT_BASE}/match-task`, task);
    return response.data;
  },

  async matchAllPending(): Promise<TaskAssignment[]> {
    const response = await geminiHttp.post<TaskAssignment[]>(`${ASSIGNMENT_BASE}/match-all-pending`);
    return response.data;
  },

  async getAssignmentHistory(personId: number): Promise<AssignmentHistory[]> {
    const response = await personnelHttp.get<AssignmentHistory[]>(`${ASSIGNMENT_BASE}/history/${personId}`);
    return response.data;
  },

  async getActiveAssignments(personId: number): Promise<AssignmentHistory[]> {
    const response = await personnelHttp.get<AssignmentHistory[]>(`${ASSIGNMENT_BASE}/active/${personId}`);
    return response.data;
  },

  async completeAssignment(assignmentId: number): Promise<AssignmentHistory> {
    const response = await personnelHttp.put<AssignmentHistory>(`${ASSIGNMENT_BASE}/${assignmentId}/complete`);
    return response.data;
  },
};
