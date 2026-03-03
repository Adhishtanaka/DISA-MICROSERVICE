import { useState } from "react";
import type { IncidentRequest, IncidentType, Severity } from "../types/incident.types";

type Props = {
  onSubmit: (values: IncidentRequest) => Promise<void>;
};

export default function IncidentForm({ onSubmit }: Props) {
  const [values, setValues] = useState<IncidentRequest>({
    type: "FLOOD",
    severity: "MEDIUM",
    description: "",
    latitude: 6.9271,
    longitude: 79.8612,
    address: "",
  });
  const [submitting, setSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState<string | null>(null);

  function set<K extends keyof IncidentRequest>(key: K, val: IncidentRequest[K]) {
    setValues((p) => ({ ...p, [key]: val }));
  }

  async function handleSubmit() {
    if (!values.description.trim() || !values.address.trim()) {
      setSubmitError("Description and address are required.");
      return;
    }
    setSubmitError(null);
    setSubmitting(true);
    try {
      await onSubmit(values);
      setValues({ type: "FLOOD", severity: "MEDIUM", description: "", latitude: 6.9271, longitude: 79.8612, address: "" });
    } catch {
      setSubmitError("Failed to create incident. Please try again.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div style={panelStyle}>
      <div style={{ fontSize: 18, fontWeight: 800, marginBottom: 10 }}>Create Incident</div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
        <label style={labelStyle}>
          Type
          <select value={values.type} onChange={(e) => set("type", e.target.value as IncidentType)} style={inputStyle}>
            <option value="EARTHQUAKE">EARTHQUAKE</option>
            <option value="FLOOD">FLOOD</option>
            <option value="FIRE">FIRE</option>
            <option value="LANDSLIDE">LANDSLIDE</option>
            <option value="TSUNAMI">TSUNAMI</option>
            <option value="CYCLONE">CYCLONE</option>
            <option value="DROUGHT">DROUGHT</option>
          </select>
        </label>

        <label style={labelStyle}>
          Severity
          <select value={values.severity} onChange={(e) => set("severity", e.target.value as Severity)} style={inputStyle}>
            <option value="LOW">LOW</option>
            <option value="MEDIUM">MEDIUM</option>
            <option value="HIGH">HIGH</option>
            <option value="CRITICAL">CRITICAL</option>
          </select>
        </label>
      </div>

      <label style={{ ...labelStyle, marginTop: 10 }}>
        Description
        <textarea
          value={values.description}
          onChange={(e) => set("description", e.target.value)}
          rows={3}
          style={{ ...inputStyle, resize: "vertical" }}
          placeholder="Describe the incident..."
        />
      </label>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, marginTop: 10 }}>
        <label style={labelStyle}>
          Latitude
          <input
            type="number"
            value={values.latitude}
            onChange={(e) => set("latitude", Number(e.target.value))}
            style={inputStyle}
          />
        </label>

        <label style={labelStyle}>
          Longitude
          <input
            type="number"
            value={values.longitude}
            onChange={(e) => set("longitude", Number(e.target.value))}
            style={inputStyle}
          />
        </label>
      </div>

      <label style={{ ...labelStyle, marginTop: 10 }}>
        Address
        <input
          value={values.address}
          onChange={(e) => set("address", e.target.value)}
          style={inputStyle}
          placeholder="City, street..."
        />
      </label>

      {submitError && (
        <div style={{ marginTop: 8, color: "#dc2626", fontSize: 13 }}>{submitError}</div>
      )}

      <div style={{ marginTop: 12, display: "flex", gap: 10 }}>
        <button
          onClick={handleSubmit}
          disabled={submitting}
          style={{
            padding: "10px 14px",
            borderRadius: 12,
            background: submitting ? "#93c5fd" : "#2563eb",
            color: "white",
            fontWeight: 800,
            border: "none",
            cursor: submitting ? "not-allowed" : "pointer",
          }}
        >
          {submitting ? "Creating..." : "Create Incident"}
        </button>

        <button
          onClick={() => setValues((v) => ({ ...v, description: "", address: "" }))}
          disabled={submitting}
          style={{
            padding: "10px 14px",
            borderRadius: 12,
            background: "white",
            border: "1px solid #E5E7EB",
            fontWeight: 800,
            cursor: "pointer",
          }}
        >
          Clear
        </button>
      </div>
    </div>
  );
}

const panelStyle: React.CSSProperties = {
  border: "1px solid #E5E7EB",
  borderRadius: 16,
  padding: 16,
  background: "white",
  boxShadow: "0 1px 12px rgba(0,0,0,0.05)",
};

const labelStyle: React.CSSProperties = {
  display: "grid",
  gap: 6,
  fontSize: 12,
  color: "#374151",
  fontWeight: 700,
};

const inputStyle: React.CSSProperties = {
  padding: "10px 12px",
  borderRadius: 12,
  border: "1px solid #E5E7EB",
  outline: "none",
};
