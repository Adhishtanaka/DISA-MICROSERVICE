import { useMemo, useState } from "react";
import type { Incident, IncidentStatus, IncidentType, Severity } from "../types/incident.types";
import IncidentCard from "./IncidentCard";

type Props = {
  incidents: Incident[];
  selectedId?: number | null;
  onSelect: (incident: Incident) => void;
};

export default function IncidentList({ incidents, selectedId, onSelect }: Props) {
  const [type, setType] = useState<IncidentType | "ALL">("ALL");
  const [severity, setSeverity] = useState<Severity | "ALL">("ALL");
  const [status, setStatus] = useState<IncidentStatus | "ALL">("ALL");
  const [q, setQ] = useState("");

  const filtered = useMemo(() => {
    return incidents.filter((i) => {
      const matchType = type === "ALL" || i.type === type;
      const matchSev = severity === "ALL" || i.severity === severity;
      const matchStatus = status === "ALL" || i.status === status;

      const text = `${i.incidentCode} ${i.type} ${i.severity} ${i.status} ${i.description} ${i.address}`.toLowerCase();
      const matchQ = q.trim() === "" || text.includes(q.toLowerCase());

      return matchType && matchSev && matchStatus && matchQ;
    });
  }, [incidents, type, severity, status, q]);

  return (
    <div>
      <div style={{ display: "flex", gap: 10, flexWrap: "wrap", marginBottom: 12 }}>
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Search (code, address, description...)"
          style={{
            flex: 1,
            minWidth: 220,
            padding: "10px 12px",
            borderRadius: 12,
            border: "1px solid #E5E7EB",
          }}
        />

        <select value={type} onChange={(e) => setType(e.target.value as any)} style={selectStyle}>
          <option value="ALL">All Types</option>
          <option value="EARTHQUAKE">EARTHQUAKE</option>
          <option value="FLOOD">FLOOD</option>
          <option value="FIRE">FIRE</option>
          <option value="LANDSLIDE">LANDSLIDE</option>
          <option value="TSUNAMI">TSUNAMI</option>
          <option value="CYCLONE">CYCLONE</option>
          <option value="DROUGHT">DROUGHT</option>
        </select>

        <select value={severity} onChange={(e) => setSeverity(e.target.value as any)} style={selectStyle}>
          <option value="ALL">All Severities</option>
          <option value="LOW">LOW</option>
          <option value="MEDIUM">MEDIUM</option>
          <option value="HIGH">HIGH</option>
          <option value="CRITICAL">CRITICAL</option>
        </select>

        <select value={status} onChange={(e) => setStatus(e.target.value as any)} style={selectStyle}>
          <option value="ALL">All Status</option>
          <option value="REPORTED">REPORTED</option>
          <option value="ACTIVE">ACTIVE</option>
          <option value="RESOLVED">RESOLVED</option>
        </select>
      </div>

      <div style={{ display: "grid", gap: 12 }}>
        {filtered.length === 0 ? (
          <div style={{ padding: 16, borderRadius: 12, border: "1px dashed #D1D5DB", color: "#6B7280" }}>
            No incidents match your filters.
          </div>
        ) : (
          filtered.map((inc) => (
            <IncidentCard
              key={inc.id}
              incident={inc}
              selected={inc.id === selectedId}
              onClick={() => onSelect(inc)}
            />
          ))
        )}
      </div>
    </div>
  );
}

const selectStyle: React.CSSProperties = {
  padding: "10px 12px",
  borderRadius: 12,
  border: "1px solid #E5E7EB",
  background: "white",
};