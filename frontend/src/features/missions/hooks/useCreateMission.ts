import { useState } from 'react';
import { missionApi } from '../api/missionApi';
import { useMissionStore } from '../store/missionStore';
import type { CreateMissionRequest } from '../types/mission.types';

export function useCreateMission() {
  const { addMission } = useMissionStore();
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createMission = async (payload: CreateMissionRequest): Promise<boolean> => {
    setIsSubmitting(true);
    setError(null);
    try {
      const mission = await missionApi.create(payload);
      addMission(mission);
      return true;
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Failed to create mission';
      setError(message);
      return false;
    } finally {
      setIsSubmitting(false);
    }
  };

  return { createMission, isSubmitting, error };
}
