import { useState } from 'react';
import { LayoutGrid, Map, Search } from 'lucide-react';
import { useTasks } from '../hooks/useTasks';
import { TaskCard } from '../components/TaskCard';
import { TaskMap } from '../components/TaskMap';
import { CreateTaskForm } from '../components/CreateTaskForm';
import { Button } from '../../../components/ui/Button';
import { Spinner } from '../../../components/ui/Spinner';
import { useRole } from '../../auth';
import type { TaskStatus } from '../types/task.types';

const statusFilters: { label: string; value: TaskStatus | 'ALL' }[] = [
  { label: 'All', value: 'ALL' },
  { label: 'Pending', value: 'PENDING' },
  { label: 'In Progress', value: 'IN_PROGRESS' },
  { label: 'Completed', value: 'COMPLETED' },
];

export function TasksPage() {
  const { tasks, loading, error, createTask, completeTask, removeTask } = useTasks();
  const [showForm, setShowForm] = useState(false);
  const [activeFilter, setActiveFilter] = useState<TaskStatus | 'ALL'>('ALL');
  const [search, setSearch] = useState('');
  const [view, setView] = useState<'list' | 'map'>('list');
  const { canCreate } = useRole();

  // Status filter then search
  const statusFiltered =
    activeFilter === 'ALL' ? tasks : tasks.filter((t) => t.status === activeFilter);

  const query = search.trim().toLowerCase();
  const displayed = query
    ? statusFiltered.filter(
        (t) =>
          t.title.toLowerCase().includes(query) ||
          t.description.toLowerCase().includes(query) ||
          t.location.toLowerCase().includes(query) ||
          t.taskCode.toLowerCase().includes(query) ||
          t.type.toLowerCase().includes(query) ||
          (t.incidentId?.toString() ?? '').includes(query),
      )
    : statusFiltered;

  const handleCreate = async (data: Parameters<typeof createTask>[0]) => {
    await createTask(data);
    setShowForm(false);
  };

  const handleComplete = async (id: number) => {
    if (confirm('Mark this task as completed?')) await completeTask(id);
  };

  const handleDelete = async (id: number) => {
    if (confirm('Delete this task? This cannot be undone.')) await removeTask(id);
  };

  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
      {/* Header */}
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">Tasks</h1>
          <p className="mt-1 text-sm text-gray-500">{tasks.length} total tasks</p>
        </div>
        {canCreate && (
          <Button onClick={() => setShowForm((v) => !v)}>
            {showForm ? 'Cancel' : 'New Task'}
          </Button>
        )}
      </div>

      {/* Create form */}
      {canCreate && showForm && (
        <div className="mb-6 rounded-xl border border-gray-200 bg-white p-6 shadow-sm">
          <h2 className="mb-4 text-lg font-semibold text-gray-900">Create Task</h2>
          <CreateTaskForm onCreate={handleCreate} onCancel={() => setShowForm(false)} />
        </div>
      )}

      {/* Toolbar: search + filters + view toggle */}
      <div className="mb-4 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        {/* Search */}
        <div className="relative w-full sm:max-w-sm">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-gray-400" />
          <input
            type="text"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            placeholder="Search by title, location, type…"
            className="w-full rounded-lg border border-gray-300 bg-white py-2 pl-9 pr-8 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30"
          />
          {search && (
            <button
              onClick={() => setSearch('')}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-600"
            >
              ×
            </button>
          )}
        </div>

        {/* Status filters + view toggle */}
        <div className="flex flex-wrap items-center gap-2">
          {statusFilters.map((f) => (
            <button
              key={f.value}
              onClick={() => setActiveFilter(f.value)}
              className={`rounded-full px-3 py-1.5 text-sm font-medium transition-colors ${
                activeFilter === f.value
                  ? 'bg-blue-600 text-white'
                  : 'border border-gray-200 bg-white text-gray-600 hover:bg-gray-100'
              }`}
            >
              {f.label}
            </button>
          ))}

          {/* List / Map toggle */}
          <div className="ml-1 flex overflow-hidden rounded-lg border border-gray-200 bg-white">
            <button
              onClick={() => setView('list')}
              title="List view"
              className={`flex items-center gap-1.5 px-3 py-1.5 text-sm transition-colors ${
                view === 'list' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <LayoutGrid className="h-4 w-4" />
            </button>
            <button
              onClick={() => setView('map')}
              title="Map view"
              className={`flex items-center gap-1.5 border-l border-gray-200 px-3 py-1.5 text-sm transition-colors ${
                view === 'map' ? 'bg-blue-600 text-white' : 'text-gray-600 hover:bg-gray-50'
              }`}
            >
              <Map className="h-4 w-4" />
            </button>
          </div>
        </div>
      </div>

      {/* Result count when searching */}
      {!loading && !error && query && (
        <p className="mb-3 text-sm text-gray-500">
          {displayed.length} result{displayed.length !== 1 ? 's' : ''} for &ldquo;{search}&rdquo;
        </p>
      )}

      {/* Loading / error */}
      {loading && (
        <div className="flex justify-center py-12">
          <Spinner />
        </div>
      )}
      {error && (
        <p className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-600">{error}</p>
      )}

      {/* Empty state */}
      {!loading && !error && displayed.length === 0 && (
        <div className="py-12 text-center text-gray-400">
          <p className="text-lg font-medium">No tasks found</p>
          <p className="mt-1 text-sm">
            {query
              ? 'Try a different search term.'
              : activeFilter !== 'ALL'
                ? 'Try a different filter.'
                : 'Create your first task to get started.'}
          </p>
        </div>
      )}

      {/* Content */}
      {!loading && !error && displayed.length > 0 && (
        view === 'map' ? (
          <TaskMap tasks={displayed} />
        ) : (
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {displayed.map((task) => (
              <TaskCard
                key={task.id}
                task={task}
                onComplete={handleComplete}
                onDelete={handleDelete}
              />
            ))}
          </div>
        )
      )}
    </div>
  );
}
