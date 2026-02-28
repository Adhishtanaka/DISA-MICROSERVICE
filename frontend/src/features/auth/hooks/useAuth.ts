import { useState } from 'react';
import { authApi } from '../api/authApi';
import { useAuthStore } from '../store/authStore';
import type { LoginRequest, RegisterRequest, UpdateUserRequest } from '../types/auth.types';

export function useLogin() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const setAuth = useAuthStore((s) => s.setAuth);

  const login = async (data: LoginRequest) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.login(data);
      setAuth(response);
      return response;
    } catch (err: any) {
      const msg = err.response?.data?.message ?? 'Invalid username or password';
      setError(msg);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { login, loading, error };
}

export function useRegister() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const setAuth = useAuthStore((s) => s.setAuth);

  const register = async (data: RegisterRequest) => {
    setLoading(true);
    setError(null);
    try {
      const response = await authApi.register(data);
      setAuth(response);
      return response;
    } catch (err: any) {
      const msg = err.response?.data?.message ?? 'Registration failed';
      setError(msg);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { register, loading, error };
}

export function useProfile() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const setUser = useAuthStore((s) => s.setUser);

  const fetchProfile = async () => {
    setLoading(true);
    setError(null);
    try {
      const profile = await authApi.getProfile();
      setUser(profile);
      return profile;
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Failed to fetch profile');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const updateProfile = async (data: UpdateUserRequest) => {
    setLoading(true);
    setError(null);
    try {
      const profile = await authApi.updateProfile(data);
      setUser(profile);
      return profile;
    } catch (err: any) {
      setError(err.response?.data?.message ?? 'Failed to update profile');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return { fetchProfile, updateProfile, loading, error };
}
