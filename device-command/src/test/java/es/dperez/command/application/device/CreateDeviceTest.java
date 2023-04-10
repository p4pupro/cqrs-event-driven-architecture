package es.dperez.command.application.device;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.command.application.device.create.CreateDevice;
import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.mapper.MapperDevice;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.repository.DeviceRepository;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceCreatedImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CreateDeviceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private KafkaDeviceCreatedImpl kafkaDeviceCreatedImpl;

    @Mock
    private MapperDevice mapperDevice;

    @InjectMocks
    private CreateDevice createDevice;

    @Test
    void should_create_device() throws JsonParsingException {
        // Given
        Device device = Device.builder().id(UUID.randomUUID().toString()).name("Macbook")
                .mark("Apple").model("Pro M1 14inch")
                .color("Space Grey").price(2250.99)
                .serviceId(UUID.randomUUID().toString())
                .creationDate(LocalDateTime.now().toString())
                .updatedDate(null)
                .build();
        CreateDeviceRequest request = CreateDeviceRequest.builder()
                .name("Macbook")
                .mark("Apple")
                .model("Pro M1 14inch")
                .color("Space Grey")
                .price(2250.99)
                .build();

        when(mapperDevice.createDeviceRequestToDevice(request)).thenReturn(device);

        // When
        createDevice.create(request);

        // Then
        verify(deviceRepository).save(device);
        verify(kafkaDeviceCreatedImpl).publishDeviceCreatedEvent(device);
    }
}
