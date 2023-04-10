package es.dperez.query.infrastructure.eventsourcing;

import com.google.gson.Gson;
import es.dperez.query.application.eventsourcing.KafkaDeleteDeviceEventListener;
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
public class KafkaDeleteDeviceEventListenerImpl implements KafkaDeleteDeviceEventListener {

  private final Logger logger = LoggerFactory.getLogger(KafkaDeleteDeviceEventListenerImpl.class);

  private final DeviceQueryService deviceQueryService;

  private final CountDownLatch latch = new CountDownLatch(3);

  public KafkaDeleteDeviceEventListenerImpl(
      final DeviceQueryService deviceQueryService) {
    this.deviceQueryService = deviceQueryService;
  }


  @KafkaListener(topics = "${message.topic.deleteDevice}")
  public void deleteListener(final ConsumerRecord<String, String> stringStringConsumerRecord) throws DeviceNotFoundException {
    final Device device = new Gson().fromJson(stringStringConsumerRecord.value(), Device.class);
    deviceQueryService.deleteDevice(device);
    logger.info("Delete device {} in reader database", device);
    latch.countDown();
  }
}
