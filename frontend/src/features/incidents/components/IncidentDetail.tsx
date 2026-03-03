import type { Incident } from "../types/incident.types";

type Props = {
  incident: Incident | null;
};

function osmEmbedUrl(lat: number, lng: number) {
  // small box around point
  const d = 0.02;
  const left = lng - d;
  const right = lng + d;
  const top = lat + d;
  const bottom = lat - d;

  return `https://www.openstreetmap.org/export/embed.html?bbox=${left}%2C${bottom}%2C${right}%2C${top}&layer=mapnik&marker=${lat}%2C${lng}`;
}

export default function IncidentDetails({ incident }: Props) {
  if (!incident) {
    return (
      <div style={panelStyle}>
        <div style={{ color: "#6B7280" }}>Select an incident to view details.</div>
      </div>
    );
  }

  return (
    <div style={panelStyle}>
      <div style={{ display: "flex", justifyContent: "space-between", gap: 12 }}>
        <div>
          <div style={{ fontSize: 12, color: "#6B7280" }}>{incident.incidentCode}</div>
          <div style={{ fontSize: 18, fontWeight: 800, marginTop: 2 }}>
            {incident.type} • {incident.status}
          </div>
          <div style={{ marginTop: 6, color: "#111827" }}>{incident.description}</div>
          <div style={{ marginTop: 8, color: "#6B7280", fontSize: 13 }}>{incident.address}</div>
        </div>

        <div style={{ textAlign: "right" }}>
          <div style={{ fontSize: 12, color: "#6B7280" }}>Severity</div>
          <div style={{ fontSize: 16, fontWeight: 800 }}>{incident.severity}</div>
        </div>
      </div>

      <div style={{ marginTop: 14, display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10 }}>
        <Info label="Latitude" value={incident.latitude} />
        <Info label="Longitude" value={incident.longitude} />
        <Info label="Reported At" value={new Date(incident.reportedAt).toLocaleString()} />
        <Info label="Updated At" value={new Date(incident.updatedAt).toLocaleString()} />
      </div>

      <div style={{ marginTop: 16 }}>
        <div style={{ fontWeight: 800, marginBottom: 10 }}>Location</div>
        <div style={{ borderRadius: 14, overflow: "hidden", border: "1px solid #E5E7EB" }}>
          <iframe
            title="map"
            src={osmEmbedUrl(incident.latitude, incident.longitude)}
            width="100%"
            height="280"
            style={{ border: 0 }}
            loading="lazy"
          />
        </div>

        <div style={{ marginTop: 10, display: "flex", gap: 10, flexWrap: "wrap" }}>
          <a
            href={`https://www.google.com/maps?q=${incident.latitude},${incident.longitude}`}
            target="_blank"
            rel="noreferrer"
            style={linkBtn}
          >
            Open in Google Maps
          </a>
          <a
            href={`https://www.openstreetmap.org/?mlat=${incident.latitude}&mlon=${incident.longitude}#map=14/${incident.latitude}/${incident.longitude}`}
            target="_blank"
            rel="noreferrer"
            style={linkBtn}
          >
            Open in OpenStreetMap
          </a>
        </div>
      </div>
    </div>
  );
}

function Info({ label, value }: { label: string; value: any }) {
  return (
    <div style={{ padding: 12, borderRadius: 12, border: "1px solid #E5E7EB", background: "#FAFAFA" }}>
      <div style={{ fontSize: 12, color: "#6B7280" }}>{label}</div>
      <div style={{ fontWeight: 800, marginTop: 3 }}>{String(value)}</div>
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

const linkBtn: React.CSSProperties = {
  padding: "10px 12px",
  borderRadius: 12,
  border: "1px solid #E5E7EB",
  background: "#fff",
  textDecoration: "none",
  color: "#111827",
  fontWeight: 700,
};