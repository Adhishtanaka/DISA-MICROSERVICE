import axios from "axios";
import { Assessment, CreateAssessmentRequest } from "../types/assessment.types";

const BASE_URL = import.meta.env.VITE_ASSESSMENT_SERVICE_URL || "http://localhost:8087/api/assessments";

export const assessmentApi = {
  async getAll() {
    const response = await axios.get<Assessment[]>(BASE_URL);
    return response.data;
  },

  async getById(id: number) {
    const response = await axios.get<Assessment>(`${BASE_URL}/${id}`);
    return response.data;
  },

  async getByIncident(incidentId: number) {
    const response = await axios.get<Assessment[]>(`${BASE_URL}/incident/${incidentId}`);
    return response.data;
  },

  async getCompleted() {
    const response = await axios.get<Assessment[]>(`${BASE_URL}/completed`);
    return response.data;
  },

  async create(data: CreateAssessmentRequest) {
    const response = await axios.post<Assessment>(BASE_URL, data);
    return response.data;
  },

  async update(id: number, data: CreateAssessmentRequest) {
    const response = await axios.put<Assessment>(`${BASE_URL}/${id}`, data);
    return response.data;
  },

  async complete(id: number) {
    const response = await axios.put<Assessment>(`${BASE_URL}/${id}/complete`);
    return response.data;
  },

  async uploadPhoto(id: number, file: File) {
    const formData = new FormData();
    formData.append("file", file);
    const response = await axios.post<Assessment>(`${BASE_URL}/${id}/photos`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return response.data;
  },

  async delete(id: number) {
    await axios.delete(`${BASE_URL}/${id}`);
  },
};
