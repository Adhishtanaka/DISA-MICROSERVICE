export type ResourceType =
  | 'FOOD'
  | 'WATER'
  | 'MEDICINE'
  | 'EQUIPMENT'
  | 'CLOTHING'
  | 'HYGIENE';

export interface Resource {
  id: number;
  resourceCode: string;
  type: ResourceType;
  name: string;
  description: string | null;
  currentStock: number;
  threshold: number;
  unit: string;
  location: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateResourceRequest {
  resourceCode: string;
  type: ResourceType;
  name: string;
  description?: string;
  currentStock: number;
  threshold: number;
  unit: string;
  location: string;
}

export type UpdateResourceRequest = CreateResourceRequest;

export interface StockUpdateRequest {
  quantity: number;
}

export type StockOperation = 'SET' | 'INCREMENT' | 'DECREMENT';
