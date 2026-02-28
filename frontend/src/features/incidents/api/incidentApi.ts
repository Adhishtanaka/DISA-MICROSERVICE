import axios from "axios";
import { Incident, PaginatedResponse } from "../types/incident.types";

const BASE_URL = import.meta.env.VITE_INCIDENT_SERVICE_URL;

export const incidentApi = {
  async getAll(page = 0, size = 10) {
    const response = await axios.get<PaginatedResponse<Incident>>(
      `${BASE_URL}?page=${page}&size=${size}`
    );
    return response.data;
  },

  async getById(id: number) {
    const response = await axios.get<Incident>(`${BASE_URL}/${id}`);
    return response.data;
  }
};