import type { Incident } from "../types/incident.types";

type Props = {
  incidents: Incident[];
};

function StatCard({
  label,
  value,
}: {
  label: string;
  value: string | number;
}) {
  return (
    <div
      style={{
        border: "1px solid #E5E7EB",
        borderRadius: 14,
        padding: 14,
        background: "#fff",
        minWidth: 160,
      }}
    >
      <div style={{ fontSize: 12, color: "#6B7280" }}>{label}</div>
      <div style={{ fontSize: 24, fontWeight: 800, marginTop: 6 }}>{value}</div>
    </div>
  );
}

export default function IncidentStats({ incidents }: Props) {
  const total = incidents.length;

  const reported = incidents.filter((i) => i.status === "REPORTED").length;
  const active = incidents.filter((i) => i.status === "ACTIVE").length;
  const resolved = incidents.filter((i) => i.status === "RESOLVED").length;

  const critical = incidents.filter((i) => i.severity === "CRITICAL").length;
  const high = incidents.filter((i) => i.severity === "HIGH").length;
  const medium = incidents.filter((i) => i.severity === "MEDIUM").length;
  const low = incidents.filter((i) => i.severity === "LOW").length;

  return (
    <div style={{ marginTop: 14 }}>
      <h3 style={{ fontSize: 14, fontWeight: 700, marginBottom: 10 }}>
        Incident Summary
      </h3>

      <div style={{ display: "flex", gap: 12, flexWrap: "wrap" }}>
        <StatCard label="Total" value={total} />
        <StatCard label="Reported" value={reported} />
        <StatCard label="Active" value={active} />
        <StatCard label="Resolved" value={resolved} />
        <StatCard label="Critical" value={critical} />
        <StatCard label="High" value={high} />
        <StatCard label="Medium" value={medium} />
        <StatCard label="Low" value={low} />
      </div>
    </div>
  );
}