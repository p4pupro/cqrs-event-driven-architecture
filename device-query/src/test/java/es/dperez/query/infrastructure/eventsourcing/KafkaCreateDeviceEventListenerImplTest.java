package es.dperez.query.infrastructure.eventsourcing;

import es.dperez.query.domain.model.Device;
import es.dperez.query.application.service.DeviceQueryService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class KafkaCreateDeviceEventListenerImplTest {

    @Mock
    private DeviceQueryService deviceQueryService;

    @InjectMocks
    private KafkaCreateDeviceEventListenerImpl listener;

    @Captor
    private ArgumentCaptor<Device> deviceCaptor;

    @Test
    void should_listen_creation_device() {
        // Given
        final String deviceJson = "{\"name\":\"Macbook\",\"mark\":\"Apple\",\"model\":\"Pro M1 14inch\",\"color\":\"Space Grey\",\"price\":2250.99}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("createDevice", 0, 0, "testKey", deviceJson);
        Device expectedDevice = Device.builder().name("Macbook").mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        doNothing().when(deviceQueryService).createDevice(any(Device.class));

        // When
        listener.createListener(record);

        // Then
        verify(deviceQueryService).createDevice(deviceCaptor.capture());
        Device capturedDevice = deviceCaptor.getValue();
        assertThat(capturedDevice).isEqualTo(expectedDevice);
    }
}

