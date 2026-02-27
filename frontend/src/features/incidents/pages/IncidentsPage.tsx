import { useIncidents } from "../hooks/useIncidents";

export default function IncidentsPage() {
  const { incidents, loading, error } = useIncidents();

  return (
    <div className="p-6">
      <h1 className="text-xl font-semibold mb-4">Incidents</h1>

      {loading && <p>Loading...</p>}
      {error && <p className="text-red-500">{error}</p>}

      <ul className="space-y-2">
        {incidents.map((incident) => (
          <li
            key={incident.id}
            className="border rounded p-3 shadow-sm bg-white"
          >
            <p className="font-medium">
              {incident.type} — {incident.severity}
            </p>
            <p className="text-sm text-gray-600">
              {incident.address}
            </p>
          </li>
        ))}
      </ul>
    </div>
  );
}