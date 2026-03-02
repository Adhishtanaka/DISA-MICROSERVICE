import axios from "axios";
import type { Incident, PaginatedResponse } from "../types/incident.types";

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
  }
};