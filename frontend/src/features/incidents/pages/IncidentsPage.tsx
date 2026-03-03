import { useMemo, useState } from "react";
import { useIncidents } from "../hooks/useIncidents";
import { useRole } from "../../auth";
import type { Incident, IncidentStatus, Severity } from "../types/incident.types";
import IncidentList from "../components/IncidentList";
import IncidentForm from "../components/IncidentForm";
import IncidentDetails from "../components/IncidentDetail";

export default function IncidentsPage() {
  const { incidents, loading, error, createIncident, changeStatus, escalateIncident, deleteIncident } = useIncidents();
  const { canOperate, canManage, canDelete } = useRole();
  const [selected, setSelected] = useState<Incident | null>(null);

  // auto-select first item when data loads
  useMemo(() => {
    if (!selected && incidents.length > 0) setSelected(incidents[0]);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [incidents]);

  // keep selected in sync after mutations
  function syncSelected(updated: Incident) {
    setSelected(updated);
  }

  async function handleCreate(values: Parameters<typeof createIncident>[0]) {
    try {
      const created = await createIncident(values);
      setSelected(created);
    } catch {
      // error is shown inside IncidentForm
    }
  }

  async function handleStatusChange(status: IncidentStatus) {
    if (!selected) return;
    try {
      const updated = await changeStatus(selected.id, status);
      syncSelected(updated);
    } catch {
      alert("Failed to update status.");
    }
  }

  async function handleEscalate() {
    if (!selected) return;
    const severities: Severity[] = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];
    const currentIdx = severities.indexOf(selected.severity);
    if (currentIdx >= severities.length - 1) {
      alert("Incident is already at CRITICAL severity.");
      return;
    }
    const newSeverity = severities[currentIdx + 1];
    const reason = prompt(`Escalate to ${newSeverity}. Enter reason (optional):`);
    if (reason === null) return; // cancelled
    try {
      const updated = await escalateIncident(selected.id, { newSeverity, reason: reason || undefined });
      syncSelected(updated);
    } catch {
      alert("Failed to escalate incident.");
    }
  }

  async function handleDelete() {
    if (!selected) return;
    if (!confirm(`Delete incident ${selected.incidentCode}? This cannot be undone.`)) return;
    try {
      await deleteIncident(selected.id);
      setSelected(null);
    } catch {
      alert("Failed to delete incident.");
    }
  }

  return (
    <div style={{ padding: 24, maxWidth: 1300, margin: "0 auto" }}>
      <div style={{ display: "flex", alignItems: "baseline", justifyContent: "space-between", gap: 12 }}>
        <h1 style={{ fontSize: 30, fontWeight: 900 }}>Incidents</h1>
        <div style={{ color: "#6B7280" }}>{incidents.length} total</div>
      </div>

      {loading && <p style={{ marginTop: 10 }}>Loading...</p>}
      {error && <p style={{ marginTop: 10, color: "crimson" }}>{error}</p>}

      <div style={{ marginTop: 18, display: "grid", gridTemplateColumns: "1.2fr 0.8fr", gap: 16 }}>
        {/* LEFT */}
        <div style={{ display: "grid", gap: 16 }}>
          {canOperate && (
            <IncidentForm onSubmit={handleCreate} />
          )}

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

          {selected && (canManage || canDelete) && (
            <div style={{ marginTop: 12, display: "grid", gap: 8 }}>
              {canManage && (
                <>
                  <div style={{ fontSize: 12, fontWeight: 700, color: "#374151", marginBottom: 2 }}>Update Status</div>
                  <div style={{ display: "flex", gap: 8, flexWrap: "wrap" }}>
                    {(["REPORTED", "ACTIVE", "RESOLVED"] as IncidentStatus[]).map((s) => (
                      <button
                        key={s}
                        onClick={() => handleStatusChange(s)}
                        disabled={selected.status === s}
                        style={{
                          padding: "7px 12px",
                          borderRadius: 10,
                          border: `1px solid ${selected.status === s ? "#2563eb" : "#E5E7EB"}`,
                          background: selected.status === s ? "#2563eb" : "white",
                          color: selected.status === s ? "white" : "#374151",
                          fontWeight: 700,
                          fontSize: 12,
                          cursor: selected.status === s ? "default" : "pointer",
                        }}
                      >
                        {s}
                      </button>
                    ))}
                  </div>

                  <button
                    onClick={handleEscalate}
                    style={{
                      marginTop: 4,
                      padding: "9px 14px",
                      borderRadius: 10,
                      border: "1px solid #f97316",
                      background: "white",
                      color: "#f97316",
                      fontWeight: 800,
                      fontSize: 13,
                      cursor: "pointer",
                    }}
                  >
                    Escalate Severity
                  </button>
                </>
              )}

              {canDelete && (
                <button
                  onClick={handleDelete}
                  style={{
                    marginTop: 4,
                    padding: "9px 14px",
                    borderRadius: 10,
                    border: "1px solid #dc2626",
                    background: "white",
                    color: "#dc2626",
                    fontWeight: 800,
                    fontSize: 13,
                    cursor: "pointer",
                  }}
                >
                  Delete Incident
                </button>
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
