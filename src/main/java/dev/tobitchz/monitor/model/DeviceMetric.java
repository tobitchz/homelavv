package dev.tobitchz.monitor.model;

import java.time.Instant;

public record DeviceMetric(
		String deviceId,
		double cpu,
		long memoryUsedMb,
		int diskUsedPercent,
		Instant receivedAt
) {
}
