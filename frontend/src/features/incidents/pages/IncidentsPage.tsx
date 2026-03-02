import { useMemo, useState } from "react";
import { useIncidents } from "../hooks/useIncidents";
import type { Incident } from "../types/incident.types";
import IncidentList from "../components/IncidentList";

import IncidentForm from "../components/IncidentForm";
import IncidentDetails from "../components/IncidentDetail";

export default function IncidentsPage() {
  const { incidents, loading, error } = useIncidents();
  const [selected, setSelected] = useState<Incident | null>(null);

  // auto-select first item when data loads
  useMemo(() => {
    if (!selected && incidents.length > 0) setSelected(incidents[0]);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [incidents]);

  return (
    <div style={{ padding: 24, maxWidth: 1300, margin: "0 auto" }}>
      <div style={{ display: "flex", alignItems: "baseline", justifyContent: "space-between", gap: 12 }}>
        <h1 style={{ fontSize: 30, fontWeight: 900 }}>Incidents</h1>
        <div style={{ color: "#6B7280" }}>List • Details • Map</div>
      </div>

      {loading && <p style={{ marginTop: 10 }}>Loading...</p>}
      {error && <p style={{ marginTop: 10, color: "crimson" }}>{error}</p>}

      <div style={{ marginTop: 18, display: "grid", gridTemplateColumns: "1.2fr 0.8fr", gap: 16 }}>
        {/* LEFT */}
        <div style={{ display: "grid", gap: 16 }}>
          <IncidentForm
            onSubmit={(v) => {
              // UI-only for now
              alert(`UI Submit:\n${JSON.stringify(v, null, 2)}`);
            }}
          />

          <div>
            <div style={{ fontWeight: 900, marginBottom: 10 }}>All incidents</div>
            <IncidentList
              incidents={incidents}
              selectedId={selected?.id ?? null}
              onSelect={(inc) => setSelected(inc)}
            />
          </div>
        </div>

        {/* RIGHT */}
        <div>
          <div style={{ fontWeight: 900, marginBottom: 10 }}>Details</div>
          <IncidentDetails incident={selected} />
        </div>
      </div>
    </div>
  );
}