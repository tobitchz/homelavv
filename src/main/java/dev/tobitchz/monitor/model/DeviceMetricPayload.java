package dev.tobitchz.monitor.model;

public record DeviceMetricPayload(
		String deviceId,
		double cpu,
		long memoryUsedMb,
		int diskUsedPercent
) {
}
