import axios from "axios";
import type { Shelter, ShelterRequest, CheckInRequest } from "../types/shelter.types";

const shelterHttp = axios.create({
  baseURL: import.meta.env.VITE_SHELTER_SERVICE_URL || "http://localhost:8085",
  headers: { "Content-Type": "application/json" },
  timeout: 10000,
});

shelterHttp.interceptors.request.use((config) => {
  const token = localStorage.getItem("auth_token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

const BASE = "/api/shelters";

export const shelterApi = {
  async getAll() {
    const response = await shelterHttp.get<Shelter[]>(BASE);
    return response.data;
  },

  async getById(id: number) {
    const response = await shelterHttp.get<Shelter>(`${BASE}/${id}`);
    return response.data;
  },

  async getAvailable() {
    const response = await shelterHttp.get<Shelter[]>(`${BASE}/available`);
    return response.data;
  },

  async getNearby(latitude: number, longitude: number, radiusKm = 50) {
    const response = await shelterHttp.get<Shelter[]>(`${BASE}/nearby`, {
      params: { latitude, longitude, radiusKm }
    });
    return response.data;
  },

  async create(data: ShelterRequest) {
    const response = await shelterHttp.post<Shelter>(BASE, data);
    return response.data;
  },

  async update(id: number, data: Partial<ShelterRequest>) {
    const response = await shelterHttp.put<Shelter>(`${BASE}/${id}`, data);
    return response.data;
  },

  async checkIn(id: number, data: CheckInRequest) {
    const response = await shelterHttp.post<Shelter>(`${BASE}/${id}/checkin`, data);
    return response.data;
  },

  async checkOut(id: number, data: CheckInRequest) {
    const response = await shelterHttp.post<Shelter>(`${BASE}/${id}/checkout`, data);
    return response.data;
  },

  async delete(id: number) {
    await shelterHttp.delete(`${BASE}/${id}`);
  }
};
