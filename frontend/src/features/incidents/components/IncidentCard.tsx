import type { Incident } from "../types/incident.types";

type Props = {
  incident: Incident;
  selected?: boolean;
  onClick?: () => void;
};

function severityBadge(sev: Incident["severity"]) {
  const map: Record<string, { bg: string; text: string }> = {
    LOW: { bg: "#E8F5E9", text: "#1B5E20" },
    MEDIUM: { bg: "#FFF8E1", text: "#E65100" },
    HIGH: { bg: "#FFF3E0", text: "#BF360C" },
    CRITICAL: { bg: "#FFEBEE", text: "#B71C1C" },
  };
  return map[sev] ?? { bg: "#EEE", text: "#333" };
}

export default function IncidentCard({ incident, selected, onClick }: Props) {
  const badge = severityBadge(incident.severity);

  return (
    <button
      onClick={onClick}
      style={{
        width: "100%",
        textAlign: "left",
        border: selected ? "2px solid #2563eb" : "1px solid #E5E7EB",
        background: selected ? "#EFF6FF" : "#fff",
        borderRadius: 14,
        padding: 16,
        boxShadow: "0 1px 10px rgba(0,0,0,0.05)",
        cursor: "pointer",
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", gap: 12 }}>
        <div>
          <div style={{ fontSize: 13, color: "#6B7280" }}>{incident.incidentCode}</div>
          <div style={{ fontSize: 16, fontWeight: 800, marginTop: 2 }}>
            {incident.type} <span style={{ color: "#9CA3AF" }}>•</span> {incident.status}
          </div>
        </div>

        <span
          style={{
            padding: "6px 10px",
            borderRadius: 999,
            fontSize: 12,
            fontWeight: 700,
            background: badge.bg,
            color: badge.text,
            height: "fit-content",
          }}
        >
          {incident.severity}
        </span>
      </div>

      <div style={{ marginTop: 10, color: "#111827" }}>{incident.description}</div>
      <div style={{ marginTop: 8, color: "#6B7280", fontSize: 13 }}>{incident.address}</div>

      <div style={{ marginTop: 10, color: "#6B7280", fontSize: 12 }}>
        Lat: {incident.latitude} &nbsp; Lng: {incident.longitude}
      </div>
    </button>
  );
}