export type UserRole = 'ADMIN' | 'COORDINATOR' | 'RESPONDER' | 'VOLUNTEER';

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
  phoneNumber?: string;
  role: UserRole;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  username: string;
  email: string;
  role: UserRole;
  fullName: string;
}

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  role: UserRole;
  fullName: string;
  phoneNumber?: string;
  createdAt: string;
  updatedAt: string;
}

export interface UpdateUserRequest {
  email?: string;
  fullName?: string;
  phoneNumber?: string;
}
