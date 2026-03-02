export default function IncidentSkeleton() {
  return (
    <div
      style={{
        border: "1px solid #E5E7EB",
        borderRadius: 14,
        padding: 16,
        background: "#F9FAFB",
        animation: "pulse 1.5s infinite",
      }}
    >
      <div style={{ height: 14, width: "40%", background: "#E5E7EB", marginBottom: 8 }} />
      <div style={{ height: 14, width: "70%", background: "#E5E7EB", marginBottom: 8 }} />
      <div style={{ height: 14, width: "50%", background: "#E5E7EB" }} />
    </div>
  );
}