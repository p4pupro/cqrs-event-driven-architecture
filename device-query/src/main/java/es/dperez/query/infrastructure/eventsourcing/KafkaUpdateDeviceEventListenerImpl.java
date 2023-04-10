package es.dperez.query.infrastructure.eventsourcing;

import com.google.gson.Gson;
import es.dperez.query.application.eventsourcing.KafkaUpdateDeviceEventListener;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.application.service.DeviceQueryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class KafkaUpdateDeviceEventListenerImpl implements KafkaUpdateDeviceEventListener {

    private final Logger logger = LoggerFactory.getLogger(KafkaUpdateDeviceEventListenerImpl.class);

    private final DeviceQueryService deviceQueryService;

    private final CountDownLatch latch = new CountDownLatch(3);

    public KafkaUpdateDeviceEventListenerImpl(
        final DeviceQueryService deviceQueryService) {
        this.deviceQueryService = deviceQueryService;
    }


    @KafkaListener(topics = "${message.topic.updateDevice}")
    public void updateListener(final ConsumerRecord<String, String> stringStringConsumerRecord) throws DeviceNotFoundException {
        final Device device = new Gson().fromJson(stringStringConsumerRecord.value(), Device.class);
        deviceQueryService.updateDevice(device);
        logger.info("Update device {} in reader database", device);
        latch.countDown();
    }
}
