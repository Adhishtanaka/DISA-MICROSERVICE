import { useAuthStore } from '../store/authStore';
import type { UserRole } from '../types/auth.types';

export function useRole() {
  const role = useAuthStore((s) => s.user?.role) as UserRole | undefined;

  const isAdmin      = role === 'ADMIN';
  const isCoordinator = role === 'COORDINATOR';
  const isResponder  = role === 'RESPONDER';
  const isVolunteer  = role === 'VOLUNTEER';

  /** ADMIN + COORDINATOR: create resources, missions, shelters, tasks, personnel */
  const canCreate = isAdmin || isCoordinator;

  /** ADMIN + COORDINATOR: update / edit any record */
  const canManage = isAdmin || isCoordinator;

  /** ADMIN + COORDINATOR + RESPONDER: report incidents, submit assessments, complete tasks */
  const canOperate = isAdmin || isCoordinator || isResponder;

  /** ADMIN only: delete any record */
  const canDelete = isAdmin;

  /** ADMIN + COORDINATOR: delete assessments */
  const canDeleteAssessment = isAdmin || isCoordinator;

  /** ADMIN + COORDINATOR + RESPONDER: update mission status */
  const canUpdateMissionStatus = isAdmin || isCoordinator || isResponder;

  return {
    role,
    isAdmin,
    isCoordinator,
    isResponder,
    isVolunteer,
    canCreate,
    canManage,
    canOperate,
    canDelete,
    canDeleteAssessment,
    canUpdateMissionStatus,
  };
}
