import axios from "axios";
import { Shelter, ShelterRequest, CheckInRequest } from "../types/shelter.types";

const BASE_URL = import.meta.env.VITE_SHELTER_SERVICE_URL || "http://localhost:8085/api/shelters";

export const shelterApi = {
  async getAll() {
    const response = await axios.get<Shelter[]>(BASE_URL);
    return response.data;
  },

  async getById(id: number) {
    const response = await axios.get<Shelter>(`${BASE_URL}/${id}`);
    return response.data;
  },

  async getAvailable() {
    const response = await axios.get<Shelter[]>(`${BASE_URL}/available`);
    return response.data;
  },

  async getNearby(latitude: number, longitude: number, radiusKm = 50) {
    const response = await axios.get<Shelter[]>(`${BASE_URL}/nearby`, {
      params: { latitude, longitude, radiusKm }
    });
    return response.data;
  },

  async create(data: ShelterRequest) {
    const response = await axios.post<Shelter>(BASE_URL, data);
    return response.data;
  },

  async update(id: number, data: Partial<ShelterRequest>) {
    const response = await axios.put<Shelter>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  async checkIn(id: number, data: CheckInRequest) {
    const response = await axios.post<Shelter>(`${BASE_URL}/${id}/checkin`, data);
    return response.data;
  },

  async checkOut(id: number, data: CheckInRequest) {
    const response = await axios.post<Shelter>(`${BASE_URL}/${id}/checkout`, data);
    return response.data;
  },

  async delete(id: number) {
    await axios.delete(`${BASE_URL}/${id}`);
  }
};
