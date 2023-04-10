package es.dperez.query.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.domain.repository.DeviceRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeviceQueryServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceQueryService deviceQueryService;

    @Test
    void should_create_device() {
        // Given
        Device device = Device.builder().name("Macbook").mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();

        // When
        deviceQueryService.createDevice(device);

        // Then
        verify(deviceRepository).save(device);
    }

    @Test
    void should_update_device() throws DeviceNotFoundException {
        // Given
        final String serviceId = "2128a214-f976-4ff3-a8c9-ebc529153449";
        Device updatedDevice = Device.builder().name("Macbook").mark("Apple").model("Pro M2 16inch").color("Silver").price(3259.99).serviceId(serviceId).build();

        when(deviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(updatedDevice));

        // When
        deviceQueryService.updateDevice(updatedDevice);

        // Then
        verify(deviceRepository).findByServiceId(serviceId);
        verify(deviceRepository).save(updatedDevice);
    }

    @Test
    void should_delete_device() throws DeviceNotFoundException {
        // Given
        final String serviceId = "2128a214-f976-4ff3-a8c9-ebc529153449";
        Device deviceToDelete = Device.builder().name("Macbook").mark("Apple").model("Pro M2 16inch").color("Silver").price(3259.99).serviceId(serviceId).build();

        when(deviceRepository.findByServiceId(serviceId)).thenReturn(Optional.of(deviceToDelete));

        // When
        deviceQueryService.deleteDevice(deviceToDelete);

        // Then
        verify(deviceRepository).findByServiceId(serviceId);
        verify(deviceRepository).delete(deviceToDelete);
    }
}