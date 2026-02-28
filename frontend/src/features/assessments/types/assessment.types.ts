export type DamageSeverity = "MINOR" | "MODERATE" | "SEVERE" | "CRITICAL";
export type AssessmentStatus = "DRAFT" | "COMPLETED";

export interface Assessment {
  id: number;
  assessmentCode: string;
  incidentId: number;
  assessorId: number;
  assessorName: string;
  severity: DamageSeverity;
  findings: string;
  recommendations: string;
  requiredActions: string[];
  photoUrls: string[];
  status: AssessmentStatus;
  estimatedCasualties: number;
  estimatedDisplaced: number;
  affectedInfrastructure: string;
  createdAt: string;
  completedAt?: string;
}

export interface CreateAssessmentRequest {
  incidentId: number;
  assessorId: number;
  assessorName: string;
  severity: DamageSeverity;
  findings: string;
  recommendations?: string;
  requiredActions?: string[];
  estimatedCasualties?: number;
  estimatedDisplaced?: number;
  affectedInfrastructure?: string;
  status?: AssessmentStatus;
}
