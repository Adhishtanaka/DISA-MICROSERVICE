import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { LoginResponse, UserProfile } from '../types/auth.types';

interface AuthState {
  token: string | null;
  user: UserProfile | null;
  isAuthenticated: boolean;
  setAuth: (response: LoginResponse) => void;
  setUser: (user: UserProfile) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>()(
  persist(
    (set) => ({
      token: null,
      user: null,
      isAuthenticated: false,

      setAuth: (response) => {
        localStorage.setItem('auth_token', response.token);
        set({
          token: response.token,
          isAuthenticated: true,
          user: {
            id: response.userId,
            username: response.username,
            email: response.email,
            role: response.role,
            fullName: response.fullName,
            createdAt: '',
            updatedAt: '',
          },
        });
      },

      setUser: (user) => set({ user }),

      logout: () => {
        localStorage.removeItem('auth_token');
        set({ token: null, user: null, isAuthenticated: false });
      },
    }),
    { name: 'disa-auth' },
  ),
);
