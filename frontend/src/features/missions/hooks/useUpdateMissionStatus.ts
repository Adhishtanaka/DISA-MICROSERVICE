import { useState } from 'react';
import { missionApi } from '../api/missionApi';
import { useMissionStore } from '../store/missionStore';
import type { MissionStatus } from '../types/mission.types';

export function useUpdateMissionStatus() {
  const { updateMission } = useMissionStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const updateStatus = async (
    id: number,
    status: MissionStatus,
    remarks?: string,
  ): Promise<boolean> => {
    setIsSubmitting(true);
    setError(null);
    try {
      const updated = await missionApi.updateStatus(id, { missionId: id, status, remarks });
      updateMission(updated);
      return true;
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Failed to update status';
      setError(message);
      return false;
    } finally {
      setIsSubmitting(false);
    }
  };

  return { updateStatus, isSubmitting, error };
}
