import { useCallback, useEffect, useRef, useState } from 'react';
import { personnelApi } from '../api/personnelApi';
import type { Person, PersonRequest, TaskDto, TaskAssignment, AssignmentHistory } from '../types/personnel.types';

export function usePersonnel() {
  const [personnel, setPersonnel] = useState<Person[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const isInitialLoad = useRef(true);

  const fetchPersonnel = useCallback(async () => {
    // Only show loading spinner on initial load, not on refetch
    if (isInitialLoad.current) {
      setLoading(true);
    }
    setError(null);
    try {
      const data = await personnelApi.getAll();
      setPersonnel(Array.isArray(data) ? data : []);
    } catch {
      setError('Failed to load personnel');
    } finally {
      setLoading(false);
      isInitialLoad.current = false;
    }
  }, []);

  useEffect(() => {
    fetchPersonnel();
  }, [fetchPersonnel]);

  const createPerson = useCallback(async (data: PersonRequest) => {
    const created = await personnelApi.create(data);
    setPersonnel((prev) => [...created, ...prev]);
    return created;
  }, []);

  const updatePerson = useCallback(async (data: PersonRequest) => {
    const updated = await personnelApi.update(data);
    setPersonnel((prev) =>
      prev.map((p) => {
        const match = updated.find((u) => u.id === p.id);
        return match ?? p;
      })
    );
    return updated;
  }, []);

  const deletePerson = useCallback(async (id: number) => {
    await personnelApi.hardDelete(id);
    setPersonnel((prev) => prev.filter((p) => p.id !== id));
  }, []);

  const getPendingTasks = useCallback(async (): Promise<TaskDto[]> => {
    return personnelApi.getPendingTasks();
  }, []);

  const matchAllPending = useCallback(async (): Promise<TaskAssignment[]> => {
    return personnelApi.matchAllPending();
  }, []);

  const matchTask = useCallback(async (task: TaskDto): Promise<TaskAssignment> => {
    return personnelApi.matchTask(task);
  }, []);

  const getAssignmentHistory = useCallback(async (personId: number): Promise<AssignmentHistory[]> => {
    return personnelApi.getAssignmentHistory(personId);
  }, []);

  const getActiveAssignments = useCallback(async (personId: number): Promise<AssignmentHistory[]> => {
    return personnelApi.getActiveAssignments(personId);
  }, []);

  const completeAssignment = useCallback(async (assignmentId: number): Promise<AssignmentHistory> => {
    return personnelApi.completeAssignment(assignmentId);
  }, []);

  return {
    personnel,
    loading,
    error,
    createPerson,
    updatePerson,
    deletePerson,
    getPendingTasks,
    matchAllPending,
    matchTask,
    getAssignmentHistory,
    getActiveAssignments,
    completeAssignment,
    refetch: fetchPersonnel,
  };
}
