package dev.tobitchz.monitor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tobitchz.monitor.model.DeviceMetricPayload;
import dev.tobitchz.monitor.service.DeviceMetricService;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final DeviceMetricService deviceMetricService;

    public MetricsController(DeviceMetricService deviceMetricService) {
        this.deviceMetricService = deviceMetricService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveMetrics(@RequestBody DeviceMetricPayload payload) {
        deviceMetricService.store(payload);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
