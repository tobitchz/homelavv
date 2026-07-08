package dev.tobitchz.monitor.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tobitchz.monitor.model.SystemStatus;
import dev.tobitchz.monitor.service.DeviceMetricService;

@RestController
@RequestMapping("/api/system")
public class SystemController {

	private final DeviceMetricService deviceMetricService;

	public SystemController(DeviceMetricService deviceMetricService) {
		this.deviceMetricService = deviceMetricService;
	}

	@GetMapping
	public SystemStatus getSystemStatus() {
		return deviceMetricService.buildSystemStatus();
	}
}
