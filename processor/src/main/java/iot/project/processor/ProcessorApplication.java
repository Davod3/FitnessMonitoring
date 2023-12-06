package iot.project.processor;

import iot.project.processor.handlers.DataHandler;
import iot.project.processor.handlers.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;

import java.io.IOException;

@SpringBootApplication
public class ProcessorApplication {

	private static final String MQTT_TOPIC = "idc/fc56339";
	private static final String MQTT_CLIENT = "processor";
	private static final String MQTT_HOSTNAME = "172.100.10.10:1883";

	@Autowired private DataHandler dataHandler;

	public static void main(String[] args) {

		SpringApplication.run(ProcessorApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void loadTrainingData() throws IOException {
		this.dataHandler.initializeTrainingDataset();
	}

	@Bean
	public MessageChannel mqttInputChannel() {
		return new DirectChannel();
	}

	@Bean
	public MessageProducer inbound() {
		MqttPahoMessageDrivenChannelAdapter adapter =
				new MqttPahoMessageDrivenChannelAdapter("tcp://" + MQTT_HOSTNAME,
						MQTT_CLIENT, MQTT_TOPIC);

		adapter.setCompletionTimeout(5000);
		adapter.setConverter(new DefaultPahoMessageConverter());
		adapter.setQos(1);
		adapter.setOutputChannel(mqttInputChannel());
		return adapter;
	}

	@Bean
	@ServiceActivator(inputChannel = "mqttInputChannel")
	public MessageHandler handler() {
		return new MessageHandler();
	}



}
