package es.dperez.command.domain.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import es.dperez.command.application.dto.CreateDeviceRequest;
import es.dperez.command.application.dto.UpdateDeviceRequest;
import es.dperez.command.domain.converter.DeviceConverter;
import es.dperez.command.domain.exception.DeviceNotFoundException;
import es.dperez.command.domain.exception.JsonParsingException;
import es.dperez.command.domain.model.Device;
import es.dperez.command.domain.service.DeviceCommandService;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceCreatedEventSourcing;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceDeletedEventSourcing;
import es.dperez.command.infrasturcture.eventsourcing.KafkaDeviceUpdatedEventSourcing;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceCreatedEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceDeleteEvent;
import es.dperez.command.infrasturcture.eventsourcing.events.DeviceUpdateEvent;
import es.dperez.command.infrasturcture.repository.DeviceRepository;
import java.time.LocalDateTime;
import java.util.Optional;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeviceCommandServiceTest {

    @Mock
    private DeviceConverter deviceConverter;

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private KafkaDeviceCreatedEventSourcing kafkaDeviceCreatedEventSourcing;

    @Mock
    private KafkaDeviceUpdatedEventSourcing kafkaDeviceUpdatedEventSourcing;

    @Mock
    private KafkaDeviceDeletedEventSourcing kafkaDeviceDeletedEventSourcing;

    @InjectMocks
    private DeviceCommandService deviceCommandService;

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

        when(deviceConverter.createDeviceRequestToDevice(request)).thenReturn(device);

        // When
        deviceCommandService.create(request);

        // Then
        verify(deviceRepository).save(device);
        verify(kafkaDeviceCreatedEventSourcing).publicCreateDeviceEvent(device);
    }

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
        when(deviceConverter.updateDeviceRequestToDevice(device, request)).thenReturn(device);

        // When
        deviceCommandService.update(name, request);

        // Then
        verify(deviceRepository).save(device);
        verify(kafkaDeviceUpdatedEventSourcing).publicUpdateDeviceEvent(device);
    }

    @Test
    void should_delete_device() throws DeviceNotFoundException, JsonParsingException {
        // Given
        final String name = "Macbook";
        Device device = Device.builder().name(name).mark("Apple").model("Pro M1 14inch").color("Space Grey").price(2250.99).build();
        when(deviceRepository.findByName(name)).thenReturn(Optional.of(device));

        // When
        deviceCommandService.delete(name);

        // Then
        verify(deviceRepository).delete(device);
        verify(kafkaDeviceDeletedEventSourcing).publicDeleteDeviceEvent(device);
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
        assertThrows(DeviceNotFoundException.class, () -> deviceCommandService.update(name, request));
        verify(deviceRepository).findByName(name);
    }

    @Test
    void should_throw_device_not_found_when_delete() {
        // Given
        String name = "Acer";
        when(deviceRepository.findByName(name)).thenReturn(Optional.empty());

        // When // Then
        assertThrows(DeviceNotFoundException.class, () -> deviceCommandService.delete(name));
        verify(deviceRepository).findByName(name);
    }
}
