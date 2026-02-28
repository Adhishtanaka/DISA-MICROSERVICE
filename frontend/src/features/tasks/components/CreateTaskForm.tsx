import { useState } from 'react';
import { Input } from '../../../components/ui/Input';
import { Button } from '../../../components/ui/Button';
import { Select } from '../../../components/ui/Select';
import type { TaskRequest, TaskType, Priority } from '../types/task.types';

const typeOptions = [
  { value: 'RESCUE_OPERATION', label: 'Rescue Operation' },
  { value: 'MEDICAL_AID', label: 'Medical Aid' },
  { value: 'DEBRIS_REMOVAL', label: 'Debris Removal' },
  { value: 'SUPPLY_DELIVERY', label: 'Supply Delivery' },
  { value: 'EVACUATION', label: 'Evacuation' },
];

const priorityOptions = [
  { value: 'LOW', label: 'Low' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HIGH', label: 'High' },
  { value: 'CRITICAL', label: 'Critical' },
];

interface CreateTaskFormProps {
  onCreate: (data: TaskRequest) => Promise<void>;
  onCancel: () => void;
}

export function CreateTaskForm({ onCreate, onCancel }: CreateTaskFormProps) {
  const [form, setForm] = useState<TaskRequest>({
    type: 'RESCUE_OPERATION',
    title: '',
    description: '',
    priority: 'MEDIUM',
    location: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const set = (field: keyof TaskRequest) =>
    (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) =>
      setForm((f) => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await onCreate(form);
    } catch {
      setError('Failed to create task. Please try again.');
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
        <Select
          label="Type"
          options={typeOptions}
          value={form.type}
          onChange={set('type') as any}
        />
        <Select
          label="Priority"
          options={priorityOptions}
          value={form.priority}
          onChange={set('priority') as any}
        />
      </div>

      <Input
        label="Title"
        placeholder="Task title"
        value={form.title}
        onChange={set('title')}
        required
      />

      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-gray-700">Description</label>
        <textarea
          className="w-full rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30"
          placeholder="Describe the task..."
          rows={3}
          value={form.description}
          onChange={(e) => setForm((f) => ({ ...f, description: e.target.value }))}
          required
        />
      </div>

      <Input
        label="Location"
        placeholder="e.g. Building 12, Main Street"
        value={form.location}
        onChange={set('location')}
        required
      />

      <Input
        label="Incident ID (optional)"
        type="number"
        placeholder="Leave blank if not linked"
        onChange={(e) =>
          setForm((f) => ({ ...f, incidentId: e.target.value ? Number(e.target.value) : undefined }))
        }
      />

      {error && (
        <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-600">{error}</p>
      )}

      <div className="flex justify-end gap-2">
        <Button type="button" variant="outline" onClick={onCancel}>
          Cancel
        </Button>
        <Button type="submit" isLoading={loading}>
          Create Task
        </Button>
      </div>
    </form>
  );
}
