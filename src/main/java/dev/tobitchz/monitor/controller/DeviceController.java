package dev.tobitchz.monitor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import dev.tobitchz.monitor.model.DeviceMetric;
import dev.tobitchz.monitor.service.DeviceMetricService;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

	private final DeviceMetricService deviceMetricService;

	public DeviceController(DeviceMetricService deviceMetricService) {
		this.deviceMetricService = deviceMetricService;
	}

	@GetMapping
	public List<DeviceMetric> listDevices() {
		return deviceMetricService.findAll();
	}

	@GetMapping("/{deviceId}")
	public DeviceMetric getDevice(@PathVariable String deviceId) {
		return deviceMetricService.findByDeviceId(deviceId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Device not found"));
	}

}
