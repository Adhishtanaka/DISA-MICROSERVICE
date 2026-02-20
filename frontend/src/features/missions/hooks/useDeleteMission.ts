import { useState } from 'react';
import { missionApi } from '../api/missionApi';
import { useMissionStore } from '../store/missionStore';

export function useDeleteMission() {
  const { removeMission } = useMissionStore();
  const [isDeleting, setIsDeleting] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const deleteMission = async (id: number): Promise<boolean> => {
    setIsDeleting(true);
    setError(null);
    try {
      await missionApi.remove(id);
      removeMission(id);
      return true;
    } catch (err: unknown) {
      const message = err instanceof Error ? err.message : 'Failed to delete mission';
      setError(message);
      return false;
    } finally {
      setIsDeleting(false);
    }
  };

  return { deleteMission, isDeleting, error };
}
