const tableBody = document.getElementById('devicesTableBody');
const serviceStatus = document.getElementById('serviceStatus');
const deviceCount = document.getElementById('deviceCount');
const lastUpdated = document.getElementById('lastUpdated');
const refreshButton = document.getElementById('refreshButton');
const clusterStatus = document.getElementById('clusterStatus');
const mqttStatus = document.getElementById('mqttStatus');
const apiStatus = document.getElementById('apiStatus');
const frontendStatus = document.getElementById('frontendStatus');
const healthyDevices = document.getElementById('healthyDevices');
const staleDevices = document.getElementById('staleDevices');
const lastHeartbeat = document.getElementById('lastHeartbeat');

async function loadDevices() {
  try {
    const [devicesResponse, systemResponse] = await Promise.all([
      fetch('/api/devices'),
      fetch('/api/system')
    ]);

    if (!devicesResponse.ok || !systemResponse.ok) {
      throw new Error('No se pudo obtener el estado del cluster');
    }

    const devices = await devicesResponse.json();
    const system = await systemResponse.json();
    renderDevices(devices);
    renderSystem(system);
    serviceStatus.textContent = 'API disponible';
    serviceStatus.style.color = '#004400';
  } catch (error) {
    serviceStatus.textContent = error.message;
    serviceStatus.style.color = '#cc0000';
    renderEmptyState();
  }
}

function renderDevices(devices) {
  if (!devices.length) {
    renderEmptyState();
    deviceCount.textContent = '0 dispositivos';
    lastUpdated.textContent = 'Sin datos aún';
    return;
  }

  tableBody.innerHTML = devices
    .map((device) => {
      const receivedAt = device.receivedAt ? new Date(device.receivedAt).toLocaleString() : '—';
      return `
        <tr>
          <td>${device.deviceId}</td>
          <td>${device.cpu.toFixed(1)}%</td>
          <td>${device.memoryUsedMb} MB</td>
          <td>${device.diskUsedPercent}%</td>
          <td>${receivedAt}</td>
        </tr>
      `;
    })
    .join('');

  deviceCount.textContent = `${devices.length} dispositivo${devices.length > 1 ? 's' : ''}`;
  lastUpdated.textContent = `Última actualización: ${new Date().toLocaleTimeString()}`;
}

function renderSystem(system) {
  clusterStatus.textContent = system.clusterName || 'cluster ready';
  mqttStatus.textContent = system.components?.['mqtt-broker'] || 'offline';
  apiStatus.textContent = system.components?.['api-server'] || 'offline';
  frontendStatus.textContent = system.components?.['frontend'] || 'offline';
  healthyDevices.textContent = `${system.healthyDevices ?? 0} nodos saludables`;
  staleDevices.textContent = `${system.staleDevices ?? 0} nodos en riesgo`;
  lastHeartbeat.textContent = `heartbeat: ${system.lastHeartbeat || 'sin datos'}`;
}

function renderEmptyState() {
  tableBody.innerHTML = `
    <tr>
      <td colspan="5" class="empty-state">Esperando métricas…</td>
    </tr>
  `;
}

refreshButton.addEventListener('click', loadDevices);
document.addEventListener('DOMContentLoaded', loadDevices);
setInterval(loadDevices, 5000);
