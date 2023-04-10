package es.dperez.command.infrasturcture.eventsourcing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;


@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class KafkaDeviceCreatedEventSourcingTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaDeviceCreatedEventSourcing kafkaDeviceCreatedEventSourcing;


    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kafkaDeviceCreatedEventSourcing, "topicCreateDevice", "create-device");
    }

    @Test
    void should_public_create_device_topic() throws JsonParsingException, JsonProcessingException {
        // Given
        final String expectedTopic = "create-device";
        final Device device = Device.builder()
            .name("Macbook").mark("Apple")
            .model("Pro M1 14inch").color("Space Grey")
            .price(2250.99).serviceId(UUID.randomUUID().toString())
            .build();

        // When
        kafkaDeviceCreatedEventSourcing.publicCreateDeviceEvent(device);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(topicCaptor.capture(), messageCaptor.capture());
        assertEquals(expectedTopic, topicCaptor.getValue());
        String expectedJson = objectMapper.writeValueAsString(device);
        assertEquals(expectedJson, messageCaptor.getValue());
    }
}
