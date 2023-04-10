package es.dperez.command.application.device;

import es.dperez.command.application.device.update.UpdateDevice;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.application.dto.mapper.MapperDevice;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.repository.DeviceRepository;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceUpdatedImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UpdateDeviceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private KafkaDeviceUpdatedImpl kafkaDeviceUpdatedImpl;

    @Mock
    private MapperDevice mapperDevice;

    @InjectMocks
    private UpdateDevice updateDevice;

    @Test
    void should_update_device() throws DeviceNotFoundException, JsonParsingException {
        // Given
        final String name = "Macbook";
        Device device = Device.builder().id(UUID.randomUUID().toString()).name("Macbook")
                .mark("Apple").model("Pro M1 14inch")
                .color("Space Grey").price(2250.99)
                .serviceId(UUID.randomUUID().toString())
                .updatedDate(LocalDateTime.now().toString())
                .build();
        UpdateDeviceRequest request = UpdateDeviceRequest.builder()
                .name(name)
                .mark("Apple")
                .model("Pro M2 16inch")
                .color("Silver")
                .price(3259.99)
                .build();
        when(deviceRepository.findByName(name)).thenReturn(Optional.of(device));
        when(mapperDevice.updateDeviceRequestToDevice(device, request)).thenReturn(device);

        // When
        updateDevice.update(name, request);

        // Then
        verify(deviceRepository).save(device);
        verify(kafkaDeviceUpdatedImpl).publishDeviceUpdatedEvent(device);
    }

    @Test
    void should_throw_device_not_found_when_update() {
        // Given
        String name = "Acer";
        UpdateDeviceRequest request = UpdateDeviceRequest.builder()
                .name(name)
                .mark("Apple")
                .model("Pro M1 14inch")
                .color("Space Grey")
                .price(2250.99)
                .build();
        when(deviceRepository.findByName(name)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(DeviceNotFoundException.class, () -> updateDevice.update(name, request));
        verify(deviceRepository).findByName(name);
    }
}
