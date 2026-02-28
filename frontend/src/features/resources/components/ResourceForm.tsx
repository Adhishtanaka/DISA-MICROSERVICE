import { type FormEvent, useState } from 'react';
import { Input } from '../../../components/ui/Input';
import { Select } from '../../../components/ui/Select';
import { Button } from '../../../components/ui/Button';
import { useCreateResource } from '../hooks/useCreateResource';
import { useUpdateResource } from '../hooks/useUpdateResource';
import type { Resource, ResourceType } from '../types/resource.types';

const TYPE_OPTIONS = [
  { value: 'FOOD',      label: 'Food' },
  { value: 'WATER',     label: 'Water' },
  { value: 'MEDICINE',  label: 'Medicine' },
  { value: 'EQUIPMENT', label: 'Equipment' },
  { value: 'CLOTHING',  label: 'Clothing' },
  { value: 'HYGIENE',   label: 'Hygiene' },
];

interface ResourceFormProps {
  resource?: Resource;          // present when editing
  onSuccess: () => void;
  onCancel: () => void;
}

export function ResourceForm({ resource, onSuccess, onCancel }: ResourceFormProps) {
  const isEdit = !!resource;

  const { createResource, isSubmitting: isCreating, error: createError } = useCreateResource();
  const { updateRes, isSubmitting: isUpdating, error: updateError } = useUpdateResource();

  const isSubmitting = isCreating || isUpdating;
  const error = createError ?? updateError;

  const [form, setForm] = useState({
    resourceCode: resource?.resourceCode ?? '',
    type:         resource?.type         ?? ('FOOD' as ResourceType),
    name:         resource?.name         ?? '',
    description:  resource?.description  ?? '',
    currentStock: resource?.currentStock ?? 0,
    threshold:    resource?.threshold    ?? 0,
    unit:         resource?.unit         ?? '',
    location:     resource?.location     ?? '',
  });

  const set = (key: keyof typeof form) => (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>,
  ) => setForm((prev) => ({ ...prev, [key]: e.target.value }));

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    const payload = {
      ...form,
      currentStock: Number(form.currentStock),
      threshold:    Number(form.threshold),
    };
    const ok = isEdit
      ? await updateRes(resource!.id, payload)
      : await createResource(payload);
    if (ok) onSuccess();
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
      {error && (
        <div className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {error}
        </div>
      )}

      {/* Row 1 */}
      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Resource Code"
          placeholder="RES-001"
          value={form.resourceCode}
          onChange={set('resourceCode')}
          required
        />
        <Select
          label="Type"
          options={TYPE_OPTIONS}
          value={form.type}
          onChange={set('type')}
          required
        />
      </div>

      {/* Row 2 */}
      <Input
        label="Name"
        placeholder="e.g. Drinking Water 500ml"
        value={form.name}
        onChange={set('name')}
        required
      />

      {/* Row 3 */}
      <div className="grid grid-cols-3 gap-4">
        <Input
          label="Current Stock"
          type="number"
          min={0}
          value={form.currentStock}
          onChange={set('currentStock')}
          required
        />
        <Input
          label="Threshold"
          type="number"
          min={0}
          value={form.threshold}
          onChange={set('threshold')}
          required
        />
        <Input
          label="Unit"
          placeholder="kg / pcs / L"
          value={form.unit}
          onChange={set('unit')}
          required
        />
      </div>

      {/* Row 4 */}
      <Input
        label="Location"
        placeholder="Warehouse A, Shelf 3"
        value={form.location}
        onChange={set('location')}
        required
      />

      {/* Description */}
      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-gray-700">Description (optional)</label>
        <textarea
          rows={2}
          placeholder="Brief description of this resource…"
          value={form.description}
          onChange={set('description')}
          className="w-full resize-none rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30"
        />
      </div>

      {/* Footer */}
      <div className="flex justify-end gap-3 pt-1">
        <Button variant="outline" type="button" onClick={onCancel} disabled={isSubmitting}>
          Cancel
        </Button>
        <Button type="submit" isLoading={isSubmitting}>
          {isEdit ? 'Save Changes' : 'Create Resource'}
        </Button>
      </div>
    </form>
  );
}
