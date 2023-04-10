package es.dperez.command.infrasturcture.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceUpdateEvent;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Component
public class KafkaDeviceUpdatedEventSourcing {

    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(KafkaDeviceUpdatedEventSourcing.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String MESSAGE_RESPONSE = "Your update request has been received";

    public KafkaDeviceUpdatedEventSourcing(final KafkaTemplate<String, String> kafkaTemplate, final ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Value(value = "${message.topic.updateDevice}")
    private String topicUpdateDevice;

    public DeviceUpdateEvent publicUpdateDeviceEvent(final Device device) throws JsonParsingException {
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
