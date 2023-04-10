package es.dperez.command.infrasturcture.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceDeleteEvent;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaDeviceDeletedEventSourcing {

    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(KafkaDeviceDeletedEventSourcing.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String MESSAGE_RESPONSE = "Your deletion request has been received";

    public KafkaDeviceDeletedEventSourcing(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Value(value = "${message.topic.deleteDevice}")
    private String topicDeleteDevice;

    public DeviceDeleteEvent publicDeleteDeviceEvent(final Device device) throws JsonParsingException {
        final var uuid = UUID.randomUUID();
        try {
            final var json = objectMapper.writeValueAsString(device);
            logger.info("Send json '{}' to topic {}", json, topicDeleteDevice);
            kafkaTemplate.send(topicDeleteDevice, json);
            return DeviceDeleteEvent.builder()
                .uuid(uuid)
                .message(MESSAGE_RESPONSE)
                .build();
        } catch (JsonProcessingException ex) {
            throw new JsonParsingException("Error parsing Json: " + device.toString(), ex);
        }
    }
}
