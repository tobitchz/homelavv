package dev.tobitchz.monitor.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
@EnableConfigurationProperties(MqttProperties.class)
@IntegrationComponentScan
public class MqttConfig {

	@Bean
	public MqttPahoClientFactory mqttClientFactory(MqttProperties mqttProperties) {
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		MqttConnectOptions options = new MqttConnectOptions();
		options.setServerURIs(new String[] { mqttProperties.brokerUrl() });
		options.setAutomaticReconnect(true);
		factory.setConnectionOptions(options);
		return factory;
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer mqttInbound(
			MqttPahoClientFactory mqttClientFactory,
			MqttProperties mqttProperties
	) {
		MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
				mqttProperties.clientId(),
				mqttClientFactory,
				mqttProperties.topic()
		);
		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

}
