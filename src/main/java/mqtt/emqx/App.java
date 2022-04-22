package mqtt.emqx;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final int qos = 2;
    private static String broker = "tcp://192.168.123.52:41883";
    private static String clientId = "emqx_test";
    private static MemoryPersistence persistence = new MemoryPersistence();

    public static void main(String[] args) {
        String subTopic = "test1topic/#";
        String pubTopic = "test1topic/1";
        String content = "Hello World";



        try {

            publishMessage("topictest/1","메시지보내기");
            subscribeMessage("topictest/1");

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

    public static void publishMessage(String content,String pubTopic) throws MqttException {

        MqttClient client = new MqttClient(broker, clientId, persistence);

        // MQTT connection option
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("emqx_test2");
        connOpts.setPassword("emqx_test_password".toCharArray());
        // retain session
        connOpts.setCleanSession(true);

        // establish a connection
        log.info("[publishMessage] Connecting to broker: {} " , broker);
        client.connect(connOpts);

        // Required parameters for message publishing
        MqttMessage message2 = new MqttMessage(content.getBytes());
        message2.setQos(qos);
        client.publish(pubTopic, message2);
        log.info("[publishMessage] Message published");

        client.disconnect();
        log.info("[publishMessage] Disconnected");
        client.close();
    }

    public static void subscribeMessage(String subTopic) throws MqttException {

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
        log.info("[subscribeMessage] Connecting to broker: {} " , broker);
        client.connect(connOpts);

        log.info("[subscribeMessage] Connected");

        // Subscribe
        client.subscribe(subTopic);

        client.disconnect();
        log.info("[subscribeMessage] Disconnected");
        client.close();
    }
}

