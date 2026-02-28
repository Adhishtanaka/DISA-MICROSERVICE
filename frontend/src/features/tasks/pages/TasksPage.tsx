import { useState } from 'react';
import { useTasks } from '../hooks/useTasks';
import { TaskCard } from '../components/TaskCard';
import { CreateTaskForm } from '../components/CreateTaskForm';
import { Button } from '../../../components/ui/Button';
import { Spinner } from '../../../components/ui/Spinner';
import type { TaskStatus } from '../types/task.types';

const filters: { label: string; value: TaskStatus | 'ALL' }[] = [
  { label: 'All', value: 'ALL' },
  { label: 'Pending', value: 'PENDING' },
  { label: 'In Progress', value: 'IN_PROGRESS' },
  { label: 'Completed', value: 'COMPLETED' },
];

export function TasksPage() {
  const { tasks, loading, error, createTask, completeTask, removeTask } = useTasks();
  const [showForm, setShowForm] = useState(false);
  const [activeFilter, setActiveFilter] = useState<TaskStatus | 'ALL'>('ALL');

  const filtered = activeFilter === 'ALL' ? tasks : tasks.filter((t) => t.status === activeFilter);

  const handleCreate = async (data: Parameters<typeof createTask>[0]) => {
    await createTask(data);
    setShowForm(false);
  };

  const handleComplete = async (id: number) => {
    if (confirm('Mark this task as completed?')) {
      await completeTask(id);
    }
  };

  const handleDelete = async (id: number) => {
    if (confirm('Delete this task? This cannot be undone.')) {
      await removeTask(id);
    }
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Tasks</h1>
          <p className="mt-1 text-sm text-gray-500">{tasks.length} total tasks</p>
        </div>
        <Button onClick={() => setShowForm((v) => !v)}>
          {showForm ? 'Cancel' : 'New Task'}
        </Button>
      </div>

      {showForm && (
        <div className="mb-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="mb-4 text-lg font-semibold text-gray-900">Create Task</h2>
          <CreateTaskForm onCreate={handleCreate} onCancel={() => setShowForm(false)} />
        </div>
      )}

      <div className="mb-4 flex gap-2">
        {filters.map((f) => (
          <button
            key={f.value}
            onClick={() => setActiveFilter(f.value)}
            className={`rounded-full px-4 py-1.5 text-sm font-medium transition-colors ${
              activeFilter === f.value
                ? 'bg-blue-600 text-white'
                : 'bg-white text-gray-600 hover:bg-gray-100 border border-gray-200'
            }`}
          >
            {f.label}
          </button>
        ))}
      </div>

      {loading && (
        <div className="flex justify-center py-12">
          <Spinner />
        </div>
      )}

      {error && (
        <p className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">{error}</p>
      )}

      {!loading && !error && filtered.length === 0 && (
        <div className="py-12 text-center text-gray-400">
          <p className="text-lg font-medium">No tasks found</p>
          <p className="mt-1 text-sm">
            {activeFilter === 'ALL' ? 'Create your first task to get started.' : 'Try a different filter.'}
          </p>
        </div>
      )}

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {filtered.map((task) => (
          <TaskCard
            key={task.id}
            task={task}
            onComplete={handleComplete}
            onDelete={handleDelete}
          />
        ))}
      </div>
    </div>
  );
}
