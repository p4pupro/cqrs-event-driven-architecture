package es.dperez.command.application.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.application.exception.FindDeviceException;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.service.DeviceCommandService;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceUpdateEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeviceCommandControllerTest {

    @Mock
    private DeviceCommandService deviceCommandService;

    @InjectMocks
    private DeviceCommandController deviceCommandController;


    @Test
    void should_create_device() throws JsonParsingException {
        // Given
        final String expectedCreationMessage = "Your creation request has been received";
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
        DeviceCreatedEvent event = DeviceCreatedEvent.builder().uuid(UUID.randomUUID()).device(device).message(expectedCreationMessage).build();
        when(deviceCommandService.create(request)).thenReturn(event);

        // When
        DeviceCreatedEvent result = deviceCommandController.createDevice(request);

        // Then
        verify(deviceCommandService).create(request);
        assertEquals(event, result);
    }

    @Test
    void should_update_device() throws DeviceNotFoundException, JsonParsingException {
        // Arrange
        final String expectedUpdatedMessage = "Your update request has been received";
        final String name = "Macbook";
        Device device = Device.builder().id(UUID.randomUUID().toString())
            .name(name).mark("Apple").model("Pro M1 14inch")
            .color("Space Grey").price(2250.99)
            .serviceId(UUID.randomUUID().toString())
            .creationDate(LocalDateTime.now().toString())
            .updatedDate(null)
            .build();
        UpdateDeviceRequest request = UpdateDeviceRequest.builder()
            .name("Macbook")
            .mark("Apple")
            .model("Pro M1 14inch")
            .color("Space Grey")
            .price(2250.99)
            .build();
        DeviceUpdateEvent event = DeviceUpdateEvent.builder().uuid(UUID.randomUUID()).device(device).message(expectedUpdatedMessage).build();
        when(deviceCommandService.update(name, request)).thenReturn(event);

        // When
        DeviceUpdateEvent result = deviceCommandController.updateDevice(name, request);

        // Then
        verify(deviceCommandService).update(name, request);
        assertEquals(event, result);
    }

    @Test
    void should_throw_not_found_exception_when_update_device() throws DeviceNotFoundException, JsonParsingException {
        // Given
        String name = "Acer";
        UpdateDeviceRequest request = UpdateDeviceRequest.builder()
            .name("Macbook")
            .mark("Apple")
            .model("Pro M1 14inch")
            .color("Space Grey")
            .price(2250.99)
            .build();
        when(deviceCommandService.update(name, request)).thenThrow(new DeviceNotFoundException(name + "Device not found"));

        // When & Then
        assertThrows(FindDeviceException.class, () -> deviceCommandController.updateDevice(name, request));
        verify(deviceCommandService).update(name, request);
    }

    @Test
    void should_delete_device() throws DeviceNotFoundException, JsonParsingException {
        // Given
        String name = "Macbook";
        final String expectedDeleteMessage = "Your deletion request has been received";
        DeviceDeleteEvent event = DeviceDeleteEvent.builder().uuid(UUID.randomUUID()).message(expectedDeleteMessage).build();
        when(deviceCommandService.delete(name)).thenReturn(event);

        // When
        DeviceDeleteEvent result = deviceCommandController.deleteDevice(name);

        // Then
        verify(deviceCommandService).delete(name);
        assertEquals(event, result);
    }

    @Test
    void should_throw_not_found_exception_when_delete_device() throws DeviceNotFoundException, JsonParsingException {
        // Given
        final String name = "Acer";
        when(deviceCommandService.delete(name)).thenThrow(new DeviceNotFoundException(name + "Device not found"));

        // When & Then
        assertThrows(FindDeviceException.class, () -> deviceCommandController.deleteDevice(name));
        verify(deviceCommandService).delete(name);
    }
}