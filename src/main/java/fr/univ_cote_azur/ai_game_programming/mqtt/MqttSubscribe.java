package fr.univ_cote_azur.ai_game_programming.mqtt;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Classe pour recevoir des messages via MQTT
 *
 */
public class MqttSubscribe implements MqttCallback {

	private static final Logger L = LogManager.getLogger();

	/**
	 * Constructeur avec des paramètres
	 *
	 * @param name -> String
	 * @param host -> String
	 * @param port -> String
	 */
	public MqttSubscribe(String name, String host, String port) {
		L.debug("Constructeur avec des parametres | name : {}, host : {}, port : {}", name, host, port);

		subscribeMqtt(name, host, port);
	}

	/**
	 * Initialise les variables pour la réception de messages et de s'inscrire au
	 * topic
	 *
	 * @param name -> String
	 * @param host -> String
	 * @param port -> String
	 */
	public void subscribeMqtt(String name, String host, String port) {
		L.debug("Initialise les variables pour la reception de messages et de s'inscrire au topic | name : {}, host : {}, port : {}",
				name, host, port);

		String broker = String.format("tcp://%s:%s", host, port);

		try {
			MqttClient client = new MqttClient(broker, MqttAsyncClient.generateClientId());
			MqttConnectOptions connOpts = new MqttConnectOptions();

			client.connect(connOpts);
			client.setCallback(this);
			client.subscribe(String.format("awale/%s", name));
		} catch (MqttException e) {
			L.error("MQTT error connection : {}", e.getMessage());
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		L.debug("");
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		L.info("");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		L.debug("");
	}
}
