import axios from "axios";

function addAuthInterceptor(instance: ReturnType<typeof axios.create>) {
  instance.interceptors.request.use((config) => {
    const token = localStorage.getItem("auth_token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
  return instance;
}

// Mission service — controller path: /missions
const api = addAuthInterceptor(
  axios.create({
    baseURL: import.meta.env.VITE_MISSION_SERVICE_URL || "http://localhost:8086",
    headers: { "Content-Type": "application/json" },
    timeout: 10000,
  })
);

// Resource service — controller path: /api/resources
export const resourceHttp = addAuthInterceptor(
  axios.create({
    baseURL: import.meta.env.VITE_RESOURCE_SERVICE_URL || "http://localhost:8089",
    headers: { "Content-Type": "application/json" },
    timeout: 10000,
  })
);

export default api;