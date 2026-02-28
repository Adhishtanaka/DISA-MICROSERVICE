export type ShelterStatus = 
  | "OPERATIONAL" 
  | "FULL" 
  | "CLOSED" 
  | "UNDER_PREPARATION";

export interface Shelter {
  id: number;
  shelterCode: string;
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  totalCapacity: number;
  currentOccupancy: number;
  status: ShelterStatus;
  contactPerson: string;
  contactNumber: string;
  facilities: string;
  createdAt: string;
  updatedAt: string;
}

export interface ShelterRequest {
  name: string;
  address: string;
  latitude: number;
  longitude: number;
  totalCapacity: number;
  status: ShelterStatus;
  contactPerson: string;
  contactNumber: string;
  facilities: string;
}

export interface CheckInRequest {
  numberOfPeople: number;
}

export interface ShelterStats {
  availableCapacity: number;
  occupancyPercentage: number;
  isFull: boolean;
}
