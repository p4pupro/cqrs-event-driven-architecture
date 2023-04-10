package es.dperez.query.infrastructure.eventsourcing;

import com.google.gson.Gson;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.domain.service.DeviceQueryService;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaDeleteDeviceEventListener {

  private final Logger logger = LoggerFactory.getLogger(KafkaDeleteDeviceEventListener.class);

  private final DeviceQueryService deviceQueryService;

  private final CountDownLatch latch = new CountDownLatch(3);

  public KafkaDeleteDeviceEventListener(
      final DeviceQueryService deviceQueryService) {
    this.deviceQueryService = deviceQueryService;
  }


  @KafkaListener(topics = "${message.topic.deleteDevice}")
  public void listen(final ConsumerRecord<String, String> stringStringConsumerRecord) throws DeviceNotFoundException {
    final Device device = new Gson().fromJson(stringStringConsumerRecord.value(), Device.class);
    deviceQueryService.deleteDevice(device);
    logger.info("Delete device {} in reader database", device);
    latch.countDown();
  }
}
