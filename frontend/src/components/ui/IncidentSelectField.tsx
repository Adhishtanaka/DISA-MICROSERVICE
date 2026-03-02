import { useState, useEffect, useRef } from 'react';
import { incidentApi } from '../../features/incidents/api/incidentApi';
import type { Incident } from '../../features/incidents/types/incident.types';

interface IncidentSelectFieldProps {
  value?: number;
  onChange: (id: number | undefined) => void;
  required?: boolean;
  label?: string;
}

function formatLabel(incident: Incident) {
  return `[${incident.incidentCode}] ${incident.type.replace(/_/g, ' ')} — ${incident.address}`;
}

function severityColor(s: string) {
  if (s === 'CRITICAL') return 'text-red-600';
  if (s === 'HIGH') return 'text-orange-500';
  if (s === 'MEDIUM') return 'text-yellow-600';
  return 'text-green-600';
}

export function IncidentSelectField({
  value,
  onChange,
  required = false,
  label = 'Incident',
}: IncidentSelectFieldProps) {
  const [incidents, setIncidents] = useState<Incident[]>([]);
  const [query, setQuery] = useState('');
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    incidentApi
      .getAll(0, 200)
      .then((data) => setIncidents(data.content))
      .catch(() => {})
      .finally(() => setLoading(false));
  }, []);

  const selected = incidents.find((i) => i.id === value);

  // Sync display text whenever selection or open state changes
  useEffect(() => {
    if (!open) {
      setQuery(selected ? formatLabel(selected) : '');
    }
  }, [selected, open]);

  // Close on outside click and restore display text
  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false);
        setQuery(selected ? formatLabel(selected) : '');
      }
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, [selected]);

  const filtered = incidents.filter((i) =>
    `${i.incidentCode} ${i.type} ${i.address} ${i.severity} ${i.status}`
      .toLowerCase()
      .includes(query.toLowerCase()),
  );

  const handleFocus = () => {
    setQuery('');
    setOpen(true);
  };

  const handleSelect = (incident: Incident) => {
    onChange(incident.id);
    setQuery(formatLabel(incident));
    setOpen(false);
  };

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    onChange(undefined);
    setQuery('');
    setOpen(false);
  };

  return (
    <div ref={containerRef} className="relative flex flex-col gap-1.5">
      <label className="text-sm font-medium text-gray-700">
        {label}
        {required && <span className="ml-0.5 text-red-500">*</span>}
      </label>

      <div className="relative">
        <input
          type="text"
          className="w-full rounded-lg border border-gray-300 bg-white py-2 pl-3 pr-8 text-sm text-gray-900 placeholder:text-gray-400 focus:border-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-500/30 disabled:bg-gray-50 disabled:text-gray-400"
          placeholder={loading ? 'Loading incidents…' : 'Search by code, type, address…'}
          value={query}
          disabled={loading}
          onFocus={handleFocus}
          onChange={(e) => {
            setQuery(e.target.value);
            setOpen(true);
          }}
        />
        {selected && (
          <button
            type="button"
            onClick={handleClear}
            className="absolute right-2 top-1/2 -translate-y-1/2 text-gray-400 hover:text-gray-700"
            aria-label="Clear selection"
          >
            ×
          </button>
        )}
      </div>

      {open && (
        <div className="absolute left-0 right-0 top-full z-50 mt-1 max-h-60 overflow-y-auto rounded-lg border border-gray-200 bg-white shadow-lg">
          {filtered.length === 0 ? (
            <p className="px-3 py-2 text-sm text-gray-400">
              {incidents.length === 0 ? 'No incidents available' : 'No matches found'}
            </p>
          ) : (
            filtered.map((incident) => (
              <button
                key={incident.id}
                type="button"
                className="flex w-full flex-col gap-0.5 border-b border-gray-50 px-3 py-2 text-left text-sm last:border-0 hover:bg-blue-50"
                onClick={() => handleSelect(incident)}
              >
                <span className="font-medium text-gray-900">
                  [{incident.incidentCode}] {incident.type.replace(/_/g, ' ')}
                </span>
                <span className="truncate text-xs text-gray-500">{incident.address}</span>
                <span className={`text-xs font-semibold ${severityColor(incident.severity)}`}>
                  {incident.severity} · {incident.status}
                </span>
              </button>
            ))
          )}
        </div>
      )}
    </div>
  );
}
