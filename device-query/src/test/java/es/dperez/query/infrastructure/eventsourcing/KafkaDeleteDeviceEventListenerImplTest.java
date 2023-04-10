package es.dperez.query.infrastructure.eventsourcing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import es.dperez.query.domain.exception.DeviceNotFoundException;
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


@ExtendWith(MockitoExtension.class)
public class KafkaDeleteDeviceEventListenerImplTest {

    @Mock
    private DeviceQueryService deviceQueryService;

    @InjectMocks
    private KafkaDeleteDeviceEventListenerImpl listener;

    @Captor
    private ArgumentCaptor<Device> deviceCaptor;

    @Test
    void should_listen_deletion_device() throws DeviceNotFoundException {
        // Given
        final String deviceJson = "{\"name\":\"Macbook\",\"mark\":\"Apple\",\"model\":\"Pro M1 14inch\",\"color\":\"Space Grey\",\"price\":2250.99}";
        ConsumerRecord<String, String> record = new ConsumerRecord<>("delete-device", 0, 0, "testKey", deviceJson);
        Device expectedDevice = Device.builder().name("Macbook").mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        doNothing().when(deviceQueryService).deleteDevice(any(Device.class));

        // When
        listener.deleteListener(record);

        // Then
        verify(deviceQueryService).deleteDevice(deviceCaptor.capture());
        Device capturedDevice = deviceCaptor.getValue();
        assertThat(capturedDevice).isEqualTo(expectedDevice);
    }
}

