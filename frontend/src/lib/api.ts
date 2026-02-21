import axios from 'axios'

function createAuthClient(baseURL: string) {
  const client = axios.create({ baseURL })
  client.interceptors.request.use(config => {
    const token = localStorage.getItem('jwt')
    if (token) config.headers['Authorization'] = `Bearer ${token}`
    return config
  })
  return client
}

const api = createAuthClient(import.meta.env.VITE_API_URL ?? '')

export const resourceHttp = createAuthClient(
  import.meta.env.VITE_RESOURCE_API_URL ?? 'http://localhost:8083',
)

export default api