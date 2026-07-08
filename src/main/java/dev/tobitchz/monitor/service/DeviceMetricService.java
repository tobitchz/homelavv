package dev.tobitchz.monitor.service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import dev.tobitchz.monitor.model.DeviceMetric;
import dev.tobitchz.monitor.model.DeviceMetricPayload;
import dev.tobitchz.monitor.model.SystemStatus;

@Service
public class DeviceMetricService {

	private final ConcurrentHashMap<String, DeviceMetric> latestByDevice = new ConcurrentHashMap<>();

	public void store(DeviceMetricPayload payload) {
		DeviceMetric metric = new DeviceMetric(
				payload.deviceId(),
				payload.cpu(),
				payload.memoryUsedMb(),
				payload.diskUsedPercent(),
				Instant.now()
		);
		latestByDevice.put(payload.deviceId(), metric);
	}

	public List<DeviceMetric> findAll() {
		return List.copyOf(latestByDevice.values());
	}

	public Optional<DeviceMetric> findByDeviceId(String deviceId) {
		return Optional.ofNullable(latestByDevice.get(deviceId));
	}

	public SystemStatus buildSystemStatus() {
		List<DeviceMetric> metrics = findAll();
		Instant now = Instant.now();
		int healthyDevices = (int) metrics.stream()
				.filter(metric -> Duration.between(metric.receivedAt(), now).getSeconds() <= 30)
				.count();
		int staleDevices = metrics.size() - healthyDevices;
		String lastHeartbeat = metrics.stream()
				.map(DeviceMetric::receivedAt)
				.max(Instant::compareTo)
				.map(Instant::toString)
				.orElse("never");

		return new SystemStatus(
				"homelab-monitor",
				Map.of("mqtt-broker", "online", "api-server", "online", "frontend", "online"),
				metrics.size(),
				healthyDevices,
				staleDevices,
				lastHeartbeat
		);
	}

}
