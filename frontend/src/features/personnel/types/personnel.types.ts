export type PersonnelStatus = 'Inactive' | 'On Leave' | 'On Duty' | 'Available';

export interface Skill {
  id?: number;
  profession: string;
  experienceYears: number;
  missionCount: number;
  level: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface EmergencyContact {
  id?: number;
  name: string;
  telephone: string;
  address: string;
  relation: string;
  note: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface Allergy {
  id?: number;
  type: string;
  allergyTo: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface ChronicCondition {
  id?: number;
  name: string;
  severity: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface PhysicalLimitation {
  id?: number;
  limitation: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface Medication {
  id?: number;
  name: string;
  dosage: string;
  frequency: string;
  treated?: boolean;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface InjuryHistory {
  id?: number;
  injuryType: string;
  date: string;
  recoveryStatus: string;
  restrictions: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface Document {
  id?: number;
  name: string;
  url: string;
  note: string;
  issueDate: string;
  expDate: string;
  issuedBy: string;
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface MedicalCondition {
  id?: number;
  bloodGroup: string;
  height: string;
  weight: string;
  allergies?: Allergy[];
  chronicConditions?: ChronicCondition[];
  physicalLimitations?: PhysicalLimitation[];
  pastInjuries?: InjuryHistory[];
  medications?: Medication[];
  createdAt?: string;
  updatedAt?: string;
  disabled?: boolean;
}

export interface Person {
  id: number;
  personalCode: string;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  address: string;
  emergencyContacts: EmergencyContact[];
  role: string;
  department: string;
  organization: string;
  rank: string;
  medicalCondition: MedicalCondition | null;
  skills: Skill[];
  status: PersonnelStatus;
  shiftStartTime: string | null;
  shiftEndTime: string | null;
  createdAt: string;
  updatedAt: string;
  disabled: boolean;
}

export type TaskType = 'RESCUE_OPERATION' | 'MEDICAL_AID' | 'DEBRIS_REMOVAL';
export type Priority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT';
export type TaskStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED';

export interface TaskDto {
  id: number;
  taskCode: string;
  type: TaskType;
  title: string;
  description: string;
  priority: Priority;
  incidentId: number | null;
  assignedTo: number | null;
  location: string;
  status: TaskStatus;
  createdAt: string;
  completedAt: string | null;
}

export interface TaskAssignment {
  assignedPerson: Person;
  task: TaskDto;
  assignmentReason: string;
  matchScore: number;
}

export interface AssignmentHistory {
  id: number;
  personId: number;
  personName: string;
  taskId: number;
  taskCode: string;
  taskTitle: string;
  taskType: string;
  assignmentReason: string;
  matchScore: number;
  status: 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  assignedAt: string;
  completedAt: string | null;
}

export interface PersonRequest {
  id?: number;
  personalCode?: string;
  firstName: string;
  lastName: string;
  phone: string;
  email: string;
  address: string;
  emergencyContacts: EmergencyContact[];
  role: string;
  department: string;
  organization: string;
  rank: string;
  medicalCondition: MedicalCondition | null;
  skills: Skill[];
  status: PersonnelStatus;
  shiftStartTime: string | null;
  shiftEndTime: string | null;
}
