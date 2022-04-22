package mqtt.sample.users;

import mqtt.emqx.App;
import mqtt.emqx.OnMessageCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class SubscriberClient11 {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static final int qos = 0;
    private static String broker = "tcp://192.168.123.52:41883";
    private static String clientId = "subscriber_client11";
    private static MemoryPersistence persistence = new MemoryPersistence();

    public static void main(String[] args) throws Exception {

        MqttClient client = new MqttClient(broker, clientId, persistence);

        String subTopic = "topic/msg";

        // MQTT connection option
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("subscriber_user11");
        connOpts.setPassword("1234".toCharArray());
        // retain session
        //구독할 때 세션을 클리어 해버리면 메시지가 날라간다. ? -> 중요 포인트 분석 필요
        connOpts.setCleanSession(true);

        // set callback
        client.setCallback(new OnMessageCallback());

        // establish a connection
        log.info("[subscribeMessage] Connecting to broker: {} ", broker);
        client.connect(connOpts);

        log.info("[subscribeMessage] Connected");

        client.subscribe(subTopic);

        // Subscribe
        while(true) {

            TimeUnit.MILLISECONDS.sleep(1000);
        }

//        client.disconnect();
//        log.info("[subscribeMessage] Disconnected");
//        client.close();

    }
}
