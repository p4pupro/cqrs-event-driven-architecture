package es.dperez.query.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.domain.converter.DeviceConverter;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.domain.model.Device;
import es.dperez.query.infrastructure.repository.DeviceRepository;
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

    @Mock
    private DeviceConverter deviceConverter;

    @InjectMocks
    private DeviceQueryService deviceQueryService;

    @Test
    void should_found_by_name_success() throws DeviceNotFoundException {
        // Given
        final String name = "Macbook";
        Device device = Device.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        DeviceResponse expectedResponse = DeviceResponse.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        when(deviceRepository.findByName(name)).thenReturn(Optional.of(device));
        when(deviceConverter.deviceToDeviceResponse(device)).thenReturn(expectedResponse);
        DeviceResponse actualResponse;

        // When
        actualResponse = deviceQueryService.findByName(name);


        // Then
        assertEquals(expectedResponse, actualResponse);
        verify(deviceRepository).findByName(name);
        verify(deviceConverter).deviceToDeviceResponse(device);
    }

    @Test
    void should_throw_device_not_found_when_find_by_name() {
        // Given
        String name = "Acer";

        // When // Then
        assertThrows(DeviceNotFoundException.class, () -> deviceQueryService.findByName(name));
        verify(deviceRepository).findByName(name);
    }

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