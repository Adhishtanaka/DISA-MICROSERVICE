import axios from 'axios';
import type { LoginRequest, LoginResponse, RegisterRequest, UpdateUserRequest, UserProfile } from '../types/auth.types';

const api = axios.create({ baseURL: import.meta.env.VITE_AUTH_SERVICE_URL });

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const authApi = {
  register: (data: RegisterRequest) =>
    api.post<LoginResponse>('/register', data).then((r) => r.data),

  login: (data: LoginRequest) =>
    api.post<LoginResponse>('/login', data).then((r) => r.data),

  validate: () =>
    api.get<boolean>('/validate').then((r) => r.data),

  getProfile: () =>
    api.get<UserProfile>('/profile').then((r) => r.data),

  updateProfile: (data: UpdateUserRequest) =>
    api.put<UserProfile>('/profile', data).then((r) => r.data),
};
