import { useEffect, useState } from 'react';
import { MapContainer, TileLayer, Marker, Popup, useMap } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import type { Task } from '../types/task.types';

// ── Geocoding (Nominatim, module-level cache) ────────────────────────────────

const geoCache = new Map<string, [number, number] | null>();

async function geocodeLocation(location: string): Promise<[number, number] | null> {
  if (geoCache.has(location)) return geoCache.get(location)!;
  try {
    const res = await fetch(
      `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(location)}&format=json&limit=1`,
      { headers: { 'User-Agent': 'DISA-DisasterManagement/1.0' } },
    );
    const data = await res.json();
    const coord: [number, number] | null = data[0]
      ? [parseFloat(data[0].lat), parseFloat(data[0].lon)]
      : null;
    geoCache.set(location, coord);
    return coord;
  } catch {
    geoCache.set(location, null);
    return null;
  }
}

// ── Marker colours ────────────────────────────────────────────────────────────

function markerColor(task: Task): string {
  if (task.status === 'COMPLETED') return '#22c55e';
  if (task.status === 'IN_PROGRESS') return '#3b82f6';
  if (task.priority === 'URGENT') return '#ef4444';
  if (task.priority === 'HIGH') return '#f97316';
  return '#eab308';
}

function makeIcon(task: Task) {
  const bg = markerColor(task);
  return L.divIcon({
    html: `<div style="width:14px;height:14px;border-radius:50%;background:${bg};border:2.5px solid white;box-shadow:0 2px 6px rgba(0,0,0,0.35)"></div>`,
    iconSize: [14, 14],
    iconAnchor: [7, 7],
    className: '',
  });
}

// ── FitBounds helper ──────────────────────────────────────────────────────────

function FitBounds({ coords }: { coords: [number, number][] }) {
  const map = useMap();
  useEffect(() => {
    if (coords.length > 0) {
      map.fitBounds(coords, { padding: [40, 40], maxZoom: 14 });
    }
  }, [coords, map]);
  return null;
}

// ── TaskMap ───────────────────────────────────────────────────────────────────

interface TaskMapProps {
  tasks: Task[];
}

type PlacedMarker = { task: Task; coords: [number, number] };

export function TaskMap({ tasks }: TaskMapProps) {
  const [markers, setMarkers] = useState<PlacedMarker[]>([]);
  const [geocoding, setGeocoding] = useState(false);
  const [failed, setFailed] = useState(0);

  useEffect(() => {
    let cancelled = false;

    function buildMarkers() {
      let failCount = 0;
      const result: PlacedMarker[] = [];
      for (const task of tasks) {
        if (!task.location) { failCount++; continue; }
        const coords = geoCache.get(task.location);
        if (coords) result.push({ task, coords });
        else if (coords === undefined) failCount++; // not yet geocoded
      }
      setMarkers(result);
      setFailed(failCount);
    }

    const uncached = [
      ...new Set(
        tasks
          .map((t) => t.location)
          .filter((loc): loc is string => !!loc && !geoCache.has(loc)),
      ),
    ];

    if (uncached.length === 0) {
      buildMarkers();
      setGeocoding(false);
      return;
    }

    setGeocoding(true);
    buildMarkers(); // show whatever is already cached immediately

    (async () => {
      for (const loc of uncached) {
        if (cancelled) return;
        await geocodeLocation(loc);
        if (!cancelled) buildMarkers(); // update map after each geocode
        await new Promise((r) => setTimeout(r, 1100)); // Nominatim: 1 req/sec
      }
      if (!cancelled) setGeocoding(false);
    })();

    return () => {
      cancelled = true;
    };
  }, [tasks]);

  const allCoords = markers.map((m) => m.coords);

  const typeLabel: Record<string, string> = {
    RESCUE_OPERATION: 'Rescue',
    MEDICAL_AID: 'Medical Aid',
    DEBRIS_REMOVAL: 'Debris Removal',
  };

  return (
    <div className="overflow-hidden rounded-xl border border-gray-200 shadow-sm">
      {/* Status bar */}
      {geocoding && (
        <div className="flex items-center gap-2 border-b border-blue-100 bg-blue-50 px-4 py-2 text-xs text-blue-700">
          <span className="inline-block h-3 w-3 animate-spin rounded-full border-2 border-blue-600 border-t-transparent" />
          Geocoding task locations via OpenStreetMap…
        </div>
      )}
      {!geocoding && failed > 0 && (
        <div className="border-b border-yellow-100 bg-yellow-50 px-4 py-2 text-xs text-yellow-700">
          {failed} task{failed !== 1 ? 's' : ''} could not be placed (unrecognised address).
        </div>
      )}

      {/* Map */}
      <MapContainer
        center={[6.9271, 79.8612]}
        zoom={10}
        style={{ height: 500 }}
        scrollWheelZoom={true}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <FitBounds coords={allCoords} />
        {markers.map(({ task, coords }) => (
          <Marker key={task.id} position={coords} icon={makeIcon(task)}>
            <Popup minWidth={200}>
              <div className="text-sm">
                <p className="font-mono text-xs text-gray-400">{task.taskCode}</p>
                <p className="mt-0.5 font-semibold text-gray-900">{task.title}</p>
                <p className="mt-1 text-xs text-gray-500">{task.location}</p>
                <div className="mt-2 flex flex-wrap gap-1 text-xs font-medium">
                  <span
                    className={`rounded px-1.5 py-0.5 ${
                      task.status === 'COMPLETED'
                        ? 'bg-green-100 text-green-700'
                        : task.status === 'IN_PROGRESS'
                          ? 'bg-blue-100 text-blue-700'
                          : 'bg-yellow-100 text-yellow-700'
                    }`}
                  >
                    {task.status.replace('_', ' ')}
                  </span>
                  <span
                    className={`rounded px-1.5 py-0.5 ${
                      task.priority === 'URGENT'
                        ? 'bg-red-100 text-red-700'
                        : task.priority === 'HIGH'
                          ? 'bg-orange-100 text-orange-700'
                          : task.priority === 'MEDIUM'
                            ? 'bg-blue-100 text-blue-700'
                            : 'bg-gray-100 text-gray-600'
                    }`}
                  >
                    {task.priority}
                  </span>
                  <span className="rounded bg-gray-100 px-1.5 py-0.5 text-gray-600">
                    {typeLabel[task.type] ?? task.type}
                  </span>
                </div>
                {task.incidentId && (
                  <p className="mt-1.5 text-xs text-gray-400">Incident #{task.incidentId}</p>
                )}
              </div>
            </Popup>
          </Marker>
        ))}
      </MapContainer>

      {/* Legend */}
      <div className="flex flex-wrap gap-4 border-t border-gray-100 bg-white px-4 py-2 text-xs text-gray-500">
        <span className="flex items-center gap-1.5">
          <span className="inline-block h-3 w-3 rounded-full bg-yellow-400" /> Pending
        </span>
        <span className="flex items-center gap-1.5">
          <span className="inline-block h-3 w-3 rounded-full bg-blue-500" /> In Progress
        </span>
        <span className="flex items-center gap-1.5">
          <span className="inline-block h-3 w-3 rounded-full bg-green-500" /> Completed
        </span>
        <span className="flex items-center gap-1.5">
          <span className="inline-block h-3 w-3 rounded-full bg-red-500" /> Urgent
        </span>
        <span className="flex items-center gap-1.5">
          <span className="inline-block h-3 w-3 rounded-full bg-orange-400" /> High Priority
        </span>
        <span className="ml-auto text-gray-400">
          {markers.length} / {tasks.length} tasks mapped
        </span>
      </div>
    </div>
  );
}
