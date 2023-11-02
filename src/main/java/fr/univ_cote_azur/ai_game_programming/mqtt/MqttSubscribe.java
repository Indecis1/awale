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
 * @author Phénix333
 */
public class MqttSubscribe implements MqttCallback {



    /**
     * Constructeur avec des paramètres
     *
     * @param name -> String
     * @param host -> String
     * @param port -> String
     */
    public MqttSubscribe(String name, String host, String port) {


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


        String broker = String.format("tcp://%s:%s", host, port);

        try {
            MqttClient client = new MqttClient(broker, MqttAsyncClient.generateClientId());
            MqttConnectOptions connOpts = new MqttConnectOptions();

            client.connect(connOpts);
            client.setCallback(this);
            client.subscribe(String.format("awale/%s", name));
        } catch (MqttException e) {

        }
    }
}

