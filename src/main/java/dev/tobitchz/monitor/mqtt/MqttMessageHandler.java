package dev.tobitchz.monitor.mqtt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.tobitchz.monitor.model.DeviceMetricPayload;
import dev.tobitchz.monitor.service.DeviceMetricService;

@Component
public class MqttMessageHandler {

	private static final Logger log = LoggerFactory.getLogger(MqttMessageHandler.class);

	private final DeviceMetricService deviceMetricService;
	private final ObjectMapper objectMapper;

	public MqttMessageHandler(DeviceMetricService deviceMetricService, ObjectMapper objectMapper) {
		this.deviceMetricService = deviceMetricService;
		this.objectMapper = objectMapper;
	}

	@ServiceActivator(inputChannel = "mqttInputChannel")
	public void handleMessage(Message<?> message) {
		try {
			String payload = message.getPayload().toString();
			DeviceMetricPayload metric = objectMapper.readValue(payload, DeviceMetricPayload.class);
			deviceMetricService.store(metric);
			log.info("Metric received from device: {}", metric.deviceId());
		} catch (Exception exception) {
			log.warn("Failed to process MQTT message: {}", exception.getMessage());
		}
	}

}
