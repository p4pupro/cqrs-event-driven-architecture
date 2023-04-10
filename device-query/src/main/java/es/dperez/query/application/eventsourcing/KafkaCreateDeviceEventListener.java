package es.dperez.query.application.eventsourcing;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public interface KafkaCreateDeviceEventListener {
    void createListener(final ConsumerRecord<String, String> stringStringConsumerRecord);
}
