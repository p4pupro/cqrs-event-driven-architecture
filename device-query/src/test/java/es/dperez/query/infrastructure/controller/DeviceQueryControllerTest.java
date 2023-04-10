package es.dperez.query.infrastructure.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.query.application.dto.DeviceResponse;
import es.dperez.query.application.exception.FindDeviceException;
import es.dperez.query.domain.exception.DeviceNotFoundException;
import es.dperez.query.application.service.DeviceQueryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeviceQueryControllerTest {

    @Mock
    private DeviceQueryService deviceQueryService;

    @InjectMocks
    private DeviceQueryController deviceQueryController;

    @Test
    void should_find_device_successfully() throws DeviceNotFoundException {
        // Given
        String name = "Macbook";
        DeviceResponse expectedResponse =  DeviceResponse.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        when(deviceQueryService.findByName(name)).thenReturn(expectedResponse);

        // When
        DeviceResponse actualResponse = deviceQueryController.findDevice(name);

        // Then
        Assertions.assertEquals(expectedResponse, actualResponse);
        verify(deviceQueryService).findByName(name);
    }

    @Test
    void should_throw_device_not_found_exception() throws DeviceNotFoundException {
        // Given
        String name = "Acer";
        when(deviceQueryService.findByName(name)).thenThrow(new DeviceNotFoundException("Error", "Device not found"));

        // When & Then
        Assertions.assertThrows(FindDeviceException.class, () -> deviceQueryController.findDevice(name));
        verify(deviceQueryService).findByName(name);
    }
}
