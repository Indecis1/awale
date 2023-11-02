package fr.univ_cote_azur.ai_game_programming.mqtt;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;



/**
 * Classe pour publier des messages via MQTT
 *
 * @author Phénix333
 */
public class MqttPublish {


    /**
     * Le nom de notre adversaire à qui envoyer le message
     */
    private String opponent;
    /**
     * Le client MQTT
     */
    private MqttClient client;

    /**
     * Constructeur avec des paramètres
     *
     * @param opponent -> String
     * @param host     -> String
     * @param port     -> String
     */
    public MqttPublish(String opponent, String host, String port) {


        this.opponent = opponent;

        String broker = String.format("tcp://%s:%s", host, port);
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            client = new MqttClient(broker, MqttAsyncClient.generateClientId(), persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();

            client.connect(connOpts);
        } catch (MqttException e) {

            // Je pars du principe que vue que le projet se base sur MQTT si la
            // fonction pour envoyer un message ne fonctionne plus le projet ne peux
            // fonctionner
            System.exit(-1);
        }
    }

    /**
     * Envoi un message sur un topic spécifique
     *
     * @param theMessage -> String : le message à envoyer
     */
    public void publish(String theMessage) {


        MqttMessage message = new MqttMessage(theMessage.getBytes());

        try {
            this.client.publish(String.format("awale/%s", this.opponent), message);
        } catch (MqttException e) {


            // Je pars du principe que vue que le projet se base sur MQTT si la
            // fonction pour envoyer un message ne fonctionne plus le projet ne peux
            // fonctionner
            System.exit(-1);
        }


    }

}
