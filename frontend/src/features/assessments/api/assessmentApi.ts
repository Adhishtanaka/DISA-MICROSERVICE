import axios from "axios";
import type { Assessment, CreateAssessmentRequest } from "../types/assessment.types";

const assessmentHttp = axios.create({
  baseURL: import.meta.env.VITE_ASSESSMENT_SERVICE_URL || "http://localhost:8087",
  headers: { "Content-Type": "application/json" },
  timeout: 10000,
});

assessmentHttp.interceptors.request.use((config) => {
  const token = localStorage.getItem("auth_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

const BASE = "/api/assessments";

export const assessmentApi = {
  async getAll() {
    const response = await assessmentHttp.get<Assessment[]>(BASE);
    return response.data;
  },

  async getById(id: number) {
    const response = await assessmentHttp.get<Assessment>(`${BASE}/${id}`);
    return response.data;
  },

  async getByIncident(incidentId: number) {
    const response = await assessmentHttp.get<Assessment[]>(`${BASE}/incident/${incidentId}`);
    return response.data;
  },

  async getCompleted() {
    const response = await assessmentHttp.get<Assessment[]>(`${BASE}/completed`);
    return response.data;
  },

  async create(data: CreateAssessmentRequest) {
    const response = await assessmentHttp.post<Assessment>(BASE, data);
    return response.data;
  },

  async update(id: number, data: CreateAssessmentRequest) {
    const response = await assessmentHttp.put<Assessment>(`${BASE}/${id}`, data);
    return response.data;
  },

  async complete(id: number) {
    const response = await assessmentHttp.put<Assessment>(`${BASE}/${id}/complete`);
    return response.data;
  },

  async uploadPhoto(id: number, file: File) {
    const formData = new FormData();
    formData.append("file", file);
    const token = localStorage.getItem("auth_token");
    const response = await assessmentHttp.post<Assessment>(`${BASE}/${id}/photos`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
        ...(token ? { Authorization: `Bearer ${token}` } : {}),
      },
    });
    return response.data;
  },

  async delete(id: number) {
    await assessmentHttp.delete(`${BASE}/${id}`);
  },
};
