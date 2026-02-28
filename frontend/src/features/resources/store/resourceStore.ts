import { create } from 'zustand';
import type { Resource, ResourceType } from '../types/resource.types';

interface ResourceFilters {
  type: ResourceType | 'ALL';
  search: string;
  lowStockOnly: boolean;
}

interface ResourceState {
  resources: Resource[];
  selectedResource: Resource | null;
  filters: ResourceFilters;
  isLoading: boolean;
  error: string | null;

  setResources: (resources: Resource[]) => void;
  addResource: (resource: Resource) => void;
  updateResource: (updated: Resource) => void;
  removeResource: (id: number) => void;
  setSelectedResource: (resource: Resource | null) => void;
  setFilters: (filters: Partial<ResourceFilters>) => void;
  setLoading: (loading: boolean) => void;
  setError: (error: string | null) => void;
}

export const useResourceStore = create<ResourceState>((set) => ({
  resources: [],
  selectedResource: null,
  filters: { type: 'ALL', search: '', lowStockOnly: false },
  isLoading: false,
  error: null,

  setResources: (resources) => set({ resources }),
  addResource: (resource) =>
    set((state) => ({ resources: [resource, ...state.resources] })),
  updateResource: (updated) =>
    set((state) => ({
      resources: state.resources.map((r) => (r.id === updated.id ? updated : r)),
      selectedResource:
        state.selectedResource?.id === updated.id ? updated : state.selectedResource,
    })),
  removeResource: (id) =>
    set((state) => ({
      resources: state.resources.filter((r) => r.id !== id),
      selectedResource: state.selectedResource?.id === id ? null : state.selectedResource,
    })),
  setSelectedResource: (resource) => set({ selectedResource: resource }),
  setFilters: (filters) =>
    set((state) => ({ filters: { ...state.filters, ...filters } })),
  setLoading: (isLoading) => set({ isLoading }),
  setError: (error) => set({ error }),
}));
