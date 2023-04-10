package es.dperez.query.infrastructure.eventsourcing;

import com.google.gson.Gson;
import es.dperez.query.application.eventsourcing.KafkaCreateDeviceEventListener;
import es.dperez.query.domain.model.Device;
import es.dperez.query.application.service.DeviceQueryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaCreateDeviceEventListenerImpl implements KafkaCreateDeviceEventListener {

    private final Logger logger = LoggerFactory.getLogger(KafkaCreateDeviceEventListenerImpl.class);

    private final DeviceQueryService deviceQueryService;

    private final CountDownLatch latch = new CountDownLatch(3);

    public KafkaCreateDeviceEventListenerImpl(DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }

    @KafkaListener(topics = "${message.topic.createDevice}")
    public void createListener(ConsumerRecord<String, String> stringStringConsumerRecord) {
        Device device = new Gson().fromJson(stringStringConsumerRecord.value(), Device.class);
        deviceQueryService.createDevice(device);
        logger.info("Insert device {} in reader database", device);
        latch.countDown();
    }
}
