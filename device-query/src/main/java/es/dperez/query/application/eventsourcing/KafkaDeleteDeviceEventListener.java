package es.dperez.query.application.eventsourcing;

import es.dperez.query.domain.exception.DeviceNotFoundException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public interface KafkaDeleteDeviceEventListener {
  void deleteListener(final ConsumerRecord<String, String> stringStringConsumerRecord) throws DeviceNotFoundException;

}
