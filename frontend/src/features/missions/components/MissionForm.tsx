import { type FormEvent, useState } from 'react';
import { Input } from '../../../components/ui/Input';
import { Select } from '../../../components/ui/Select';
import { Button } from '../../../components/ui/Button';
import { useCreateMission } from '../hooks/useCreateMission';
import type { CreateMissionRequest, MissionType } from '../types/mission.types';

const TYPE_OPTIONS = [
  { value: 'DELIVERY', label: 'Delivery' },
  { value: 'RESCUE', label: 'Rescue' },
  { value: 'EVACUATION', label: 'Evacuation' },
  { value: 'MEDICAL_TRANSPORT', label: 'Medical Transport' },
  { value: 'ASSESMENT', label: 'Assessment' },
];

interface MissionFormProps {
  onSuccess?: () => void;
  onCancel?: () => void;
}

type FormErrors = Partial<Record<keyof CreateMissionRequest, string>>;

export function MissionForm({ onSuccess, onCancel }: MissionFormProps) {
  const { createMission, isSubmitting, error } = useCreateMission();

  const [form, setForm] = useState<CreateMissionRequest>({
    type: 'DELIVERY',
    origin: '',
    destination: '',
    description: '',
    cargoDetails: '',
    vehicleType: '',
  });

  const [errors, setErrors] = useState<FormErrors>({});

  const validate = (): boolean => {
    const e: FormErrors = {};
    if (!form.type) e.type = 'Mission type is required';
    if (!form.origin.trim()) e.origin = 'Origin is required';
    if (!form.destination.trim()) e.destination = 'Destination is required';
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleChange = <K extends keyof CreateMissionRequest>(
    key: K,
    value: CreateMissionRequest[K],
  ) => {
    setForm((prev) => ({ ...prev, [key]: value }));
    if (errors[key]) setErrors((prev) => ({ ...prev, [key]: undefined }));
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!validate()) return;
    const ok = await createMission(form);
    if (ok) onSuccess?.();
  };

  return (
    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
      {error && (
        <div className="rounded-lg bg-red-50 px-4 py-3 text-sm text-red-700 border border-red-200">
          {error}
        </div>
      )}

      <Select
        label="Mission Type *"
        options={TYPE_OPTIONS}
        value={form.type}
        error={errors.type}
        onChange={(e) => handleChange('type', e.target.value as MissionType)}
      />

      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Origin *"
          placeholder="e.g. Colombo"
          value={form.origin}
          error={errors.origin}
          onChange={(e) => handleChange('origin', e.target.value)}
        />
        <Input
          label="Destination *"
          placeholder="e.g. Galle"
          value={form.destination}
          error={errors.destination}
          onChange={(e) => handleChange('destination', e.target.value)}
        />
      </div>

      <Input
        label="Vehicle Type"
        placeholder="e.g. Truck, Ambulance"
        value={form.vehicleType}
        onChange={(e) => handleChange('vehicleType', e.target.value)}
      />

      <Input
        label="Cargo Details"
        placeholder="e.g. Food supplies - 500 boxes"
        value={form.cargoDetails}
        onChange={(e) => handleChange('cargoDetails', e.target.value)}
      />

      <div className="flex flex-col gap-1.5">
        <label className="text-sm font-medium text-gray-700">Description</label>
        <textarea
          rows={3}
          placeholder="Mission description…"
          value={form.description}
          onChange={(e) => handleChange('description', e.target.value)}
          className="w-full rounded-lg border border-gray-300 bg-white px-3 py-2 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30 resize-none"
        />
      </div>

      <div className="flex justify-end gap-3 pt-2">
        {onCancel && (
          <Button type="button" variant="outline" onClick={onCancel} disabled={isSubmitting}>
            Cancel
          </Button>
        )}
        <Button type="submit" isLoading={isSubmitting}>
          Create Mission
        </Button>
      </div>
    </form>
  );
}
