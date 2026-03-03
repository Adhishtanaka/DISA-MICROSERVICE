import axios from "axios";
import type { Incident, IncidentRequest, EscalateRequest, IncidentStatus, PaginatedResponse } from "../types/incident.types";

const incidentHttp = axios.create({
  baseURL: import.meta.env.VITE_INCIDENT_SERVICE_URL || "http://localhost:8083",
  headers: { "Content-Type": "application/json" },
  timeout: 10000,
});

incidentHttp.interceptors.request.use((config) => {
  const token = localStorage.getItem("auth_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const incidentApi = {
  async getAll(page = 0, size = 10) {
    const response = await incidentHttp.get<PaginatedResponse<Incident>>(
      `/api/incidents?page=${page}&size=${size}`
    );
    return response.data;
  },

  async getById(id: number) {
    const response = await incidentHttp.get<Incident>(`/api/incidents/${id}`);
    return response.data;
  },

  async create(data: IncidentRequest) {
    const response = await incidentHttp.post<Incident>(`/api/incidents`, data);
    return response.data;
  },

  async update(id: number, data: IncidentRequest) {
    const response = await incidentHttp.put<Incident>(`/api/incidents/${id}`, data);
    return response.data;
  },

  async escalate(id: number, data: EscalateRequest) {
    const response = await incidentHttp.put<Incident>(`/api/incidents/${id}/escalate`, data);
    return response.data;
  },

  async updateStatus(id: number, status: IncidentStatus) {
    const response = await incidentHttp.put<Incident>(`/api/incidents/${id}/status?status=${status}`);
    return response.data;
  },

  async remove(id: number) {
    await incidentHttp.delete(`/api/incidents/${id}`);
  },

  async getByStatus(status: IncidentStatus) {
    const response = await incidentHttp.get<Incident[]>(`/api/incidents/status/${status}`);
    return response.data;
  },
};