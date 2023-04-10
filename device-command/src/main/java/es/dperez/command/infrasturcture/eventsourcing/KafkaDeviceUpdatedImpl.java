package es.dperez.command.infrasturcture.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dperez.command.application.eventsourcing.EventUpdatedPublisher;
import es.dperez.command.application.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaDeviceUpdatedImpl implements EventUpdatedPublisher {

    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(KafkaDeviceUpdatedImpl.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String MESSAGE_RESPONSE = "Your update request has been received";

    public KafkaDeviceUpdatedImpl(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Value(value = "${message.topic.updateDevice}")
    private String topicUpdateDevice;

    public DeviceUpdateEvent publishDeviceUpdatedEvent(final Device device) throws JsonParsingException {
        final var uuid = UUID.randomUUID();
        try {
            final var json = objectMapper.writeValueAsString(device);
            logger.info("Send json '{}' to topic {}", json, topicUpdateDevice);
            kafkaTemplate.send(topicUpdateDevice, json);
            return DeviceUpdateEvent.builder()
                .uuid(uuid)
                .device(device)
                .message(MESSAGE_RESPONSE)
                .build();
        } catch (JsonProcessingException ex) {
            throw new JsonParsingException("Error parsing Json: " + device.toString(), ex);
        }
    }
}
