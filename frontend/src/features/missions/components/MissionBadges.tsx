import { Badge } from '../../../components/ui/Badge';
import type { MissionStatus, MissionType } from '../types/mission.types';

// ─── Status Badge ─────────────────────────────────────────────────────────────

const statusMap: Record<MissionStatus, { label: string; variant: 'pending' | 'in-progress' | 'completed' | 'cancelled' }> = {
  PENDING: { label: 'Pending', variant: 'pending' },
  IN_PROGRESS: { label: 'In Progress', variant: 'in-progress' },
  COMPLETED: { label: 'Completed', variant: 'completed' },
  CANCELLED: { label: 'Cancelled', variant: 'cancelled' },
};

export function MissionStatusBadge({ status }: { status: MissionStatus }) {
  const { label, variant } = statusMap[status] ?? { label: status, variant: 'default' as const };
  return <Badge variant={variant}>{label}</Badge>;
}

// ─── Type Badge ───────────────────────────────────────────────────────────────

const typeMap: Record<MissionType, string> = {
  DELIVERY: 'Delivery',
  RESCUE: 'Rescue',
  EVACUATION: 'Evacuation',
  MEDICAL_TRANSPORT: 'Medical Transport',
  ASSESMENT: 'Assessment',
};

export function MissionTypeBadge({ type }: { type: MissionType }) {
  return <Badge variant="blue">{typeMap[type] ?? type}</Badge>;
}
