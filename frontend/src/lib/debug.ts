/**
 * API Debugging Utility
 * Run in browser console to diagnose API connection issues
 * Usage: window.debugAPI() or import { debugAPI } from './debug'
 */

import { getBackendHealth, assessmentAPI } from './api'

export interface APIDebugInfo {
  timestamp: string
  backendHealth: Awaited<ReturnType<typeof getBackendHealth>>
  apiUrl: string
  hasJWT: boolean
  testUptime: {
    endpoint: string
    status: 'OK' | 'ERROR' | 'TIMEOUT'
    latency?: number
    error?: string
  }
}

export async function debugAPI(): Promise<APIDebugInfo> {
  console.group('🔍 API Debug Information')
  
  const timestamp = new Date().toISOString()
  console.log('📅 Timestamp:', timestamp)
  
  // Check backend health
  console.log('🏥 Checking backend health...')
  const backendHealth = await getBackendHealth()
  console.table(backendHealth)
  
  // Check JWT token
  const hasJWT = !!localStorage.getItem('jwt')
  console.log('🔐 JWT Token:', hasJWT ? '✅ Present' : '❌ Not found')
  
  // Test API endpoint
  console.log('🧪 Testing API endpoint...')
  const startTime = performance.now()
  let testStatus: 'OK' | 'ERROR' | 'TIMEOUT' = 'OK'
  let testError: string | undefined
  let latency: number | undefined
  
  try {
    const response = await assessmentAPI.getAllAssessments()
    latency = performance.now() - startTime
    console.log(`✅ API Response (${latency.toFixed(2)}ms):`, response.data)
  } catch (error: any) {
    testStatus = 'ERROR'
    testError = error.userMessage || error.message
    latency = performance.now() - startTime
    console.error(`❌ API Error (${latency.toFixed(2)}ms):`, testError)
  }
  
  // Create debug info object
  const debugInfo: APIDebugInfo = {
    timestamp,
    backendHealth,
    apiUrl: backendHealth.apiUrl,
    hasJWT,
    testUptime: {
      endpoint: 'GET /api/assessments',
      status: testStatus,
      latency: latency ? parseFloat(latency.toFixed(2)) : undefined,
      error: testError
    }
  }
  
  // Print summary
  console.group('📊 Summary')
  console.log('Backend Status:', backendHealth.isHealthy ? '✅ Connected' : '⚠️ Using Mock Data')
  console.log('API Response Time:', testStatus === 'OK' ? `${latency?.toFixed(2)}ms` : 'Failed')
  console.log('Authentication:', hasJWT ? '✅ Configured' : '⚠️ Not configured')
  console.groupEnd()
  
  console.groupEnd()
  
  return debugInfo
}

export function logCRUDOperation(
  operation: 'CREATE' | 'READ' | 'UPDATE' | 'DELETE',
  endpoint: string,
  data?: any,
  response?: any
) {
  const colors = {
    CREATE: 'color: #10B981; font-weight: bold;',
    READ: 'color: #3B82F6; font-weight: bold;',
    UPDATE: 'color: #F59E0B; font-weight: bold;',
    DELETE: 'color: #EF4444; font-weight: bold;'
  }
  
  console.group(`%c[${operation}] ${endpoint}`, colors[operation])
  if (data) console.log('Request Data:', data)
  if (response) console.log('Response:', response)
  console.groupEnd()
}

export function enableDetailedLogging(enabled: boolean = true) {
  if (enabled) {
    console.log(
      '%c✅ Detailed API Logging Enabled\n\nUse %cdebugAPI()%c to get full diagnostic info',
      'color: #10B981; font-weight: bold;',
      'color: #3B82F6; background: #E0F2FE; padding: 2px 6px; border-radius: 3px; font-weight: bold;',
      'color: #10B981; font-weight: bold;'
    )
  }
}

// Export debug API to window object in development
if (import.meta.env.DEV) {
  (window as any).debugAPI = debugAPI
  enableDetailedLogging()
}
