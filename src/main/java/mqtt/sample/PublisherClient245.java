package mqtt.sample;

import mqtt.emqx.App;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class PublisherClient245 {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final int qos = 0;
    private static String broker = "tcp://192.168.123.245:41883";
    private static String clientId = "publisher_client245";
    private static MemoryPersistence persistence = new MemoryPersistence();


    public static void main(String[] args) throws Exception{

        MqttClient client = new MqttClient(broker, clientId, persistence);

        String pubTopic = "topic/msg245";
        String content = "메시지좀 받아라245";

        // MQTT connection option
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("publisher_user245");
        connOpts.setPassword("1234".toCharArray());
        // retain session
        connOpts.setCleanSession(true); //default

        // establish a connection
        log.info("[publishMessage] Connecting to broker: {} " , broker);
        client.connect(connOpts);
        int count = 0;
        while(true) {
            // Required parameters for message publishing
            MqttMessage message = new MqttMessage((content+ " " + count++).getBytes());
            message.setQos(qos);
            client.publish(pubTopic, message );
            log.info("[publishMessage] Message published : " + count);

            TimeUnit.MILLISECONDS.sleep(5000);
        }
//        client.disconnect();
//        log.info("[publishMessage] Disconnected");
//        client.close();
//
    }
}
