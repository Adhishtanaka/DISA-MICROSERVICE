import { useEffect, useState } from 'react';
import { taskApi } from '../api/taskApi';
import type { Task, TaskRequest, AssignTaskRequest } from '../types/task.types';

export function useTasks() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchTasks = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await taskApi.getAll();
      setTasks(data);
    } catch {
      setError('Failed to load tasks');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  const createTask = async (data: TaskRequest) => {
    const task = await taskApi.create(data);
    setTasks((prev) => [task, ...prev]);
    return task;
  };

  const assignTask = async (id: number, data: AssignTaskRequest) => {
    const updated = await taskApi.assign(id, data);
    setTasks((prev) => prev.map((t) => (t.id === id ? updated : t)));
    return updated;
  };

  const completeTask = async (id: number) => {
    const updated = await taskApi.complete(id);
    setTasks((prev) => prev.map((t) => (t.id === id ? updated : t)));
    return updated;
  };

  const removeTask = async (id: number) => {
    await taskApi.remove(id);
    setTasks((prev) => prev.filter((t) => t.id !== id));
  };

  return { tasks, loading, error, createTask, assignTask, completeTask, removeTask, refetch: fetchTasks };
}
