package dev.tobitchz.monitor.model;

import java.util.Map;

public record SystemStatus(
        String clusterName,
        Map<String, String> components,
        int deviceCount,
        int healthyDevices,
        int staleDevices,
        String lastHeartbeat
) {
}
