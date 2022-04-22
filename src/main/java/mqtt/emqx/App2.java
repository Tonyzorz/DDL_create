package mqtt.emqx;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App2 {

    private static final Logger log = LoggerFactory.getLogger(App2.class);

    public static void main(String[] args) {
        String subTopic = "test1topic/#";
        String pubTopic = "test1topic/1";
        String pubTopic2 = "test1topic/2";
        String content = "Hello World";
        String content2 = "Hello World2";
        int qos = 2;
        String broker = "tcp://192.168.123.52:41883";
        String clientId = "emqx_test";
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);

            // MQTT connection option
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName("emqx_test");
            connOpts.setPassword("emqx_test_password".toCharArray());
            // retain session
            connOpts.setCleanSession(true);

            // set callback
            client.setCallback(new OnMessageCallback());

            // establish a connection
            log.info("Connecting to broker: {} " , broker);
            client.connect(connOpts);

            log.info("Connected");


            // Subscribe
            client.subscribe(subTopic);

            // Required parameters for message publishing
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            client.publish(pubTopic, message);
            log.info("Message published");

            // Required parameters for message publishing
            MqttMessage message2 = new MqttMessage(content2.getBytes());
            message2.setQos(qos);
            client.publish(pubTopic2, message2);
            log.info("Message published2");

            client.disconnect();
            log.info("Disconnected");
            client.close();
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }
}

