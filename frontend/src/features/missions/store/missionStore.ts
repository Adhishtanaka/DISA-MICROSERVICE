import { create } from 'zustand';
import type { Mission, MissionStatus, MissionType } from '../types/mission.types';

interface MissionFilters {
  status: MissionStatus | 'ALL';
  type: MissionType | 'ALL';
  search: string;
}

interface MissionState {
  missions: Mission[];
  selectedMission: Mission | null;
  filters: MissionFilters;
  isLoading: boolean;
  error: string | null;

  // Actions
  setMissions: (missions: Mission[]) => void;
  addMission: (mission: Mission) => void;
  updateMission: (updated: Mission) => void;
  removeMission: (id: number) => void;
  setSelectedMission: (mission: Mission | null) => void;
  setFilters: (filters: Partial<MissionFilters>) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}

export const useMissionStore = create<MissionState>((set) => ({
  missions: [],
  selectedMission: null,
  filters: { status: 'ALL', type: 'ALL', search: '' },
  isLoading: false,
  error: null,

  setMissions: (missions) => set({ missions }),
  addMission: (mission) =>
    set((state) => ({ missions: [mission, ...state.missions] })),
  updateMission: (updated) =>
    set((state) => ({
      missions: state.missions.map((m) => (m.id === updated.id ? updated : m)),
    })),
  removeMission: (id) =>
    set((state) => ({
      missions: state.missions.filter((m) => m.id !== id),
    })),
  setSelectedMission: (mission) => set({ selectedMission: mission }),
  setFilters: (filters) =>
    set((state) => ({ filters: { ...state.filters, ...filters } })),
  setLoading: (isLoading) => set({ isLoading }),
  setError: (error) => set({ error }),
}));
