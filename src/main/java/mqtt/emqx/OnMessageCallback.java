package mqtt.emqx;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnMessageCallback implements MqttCallback {

    private static final Logger log = LoggerFactory.getLogger(OnMessageCallback.class);

    public void connectionLost(Throwable cause) {
        // After the connection is lost, it usually reconnects here
        log.info("disconnect，you can reconnect");
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // The messages obtained after subscribe will be executed here
        log.info("Received message topic: {} " , topic);
        log.info("Received message Qos: {} " , message.getQos());
        log.info("Received message content: {} " , new String(message.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info("deliveryComplete--------- {} " , token.isComplete());
    }
}
